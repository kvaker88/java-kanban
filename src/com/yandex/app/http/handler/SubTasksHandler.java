package com.yandex.app.http.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.task.SubTask;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;

public class SubTasksHandler extends BaseHttpHandler {
    public SubTasksHandler(InMemoryTasksManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            switch (exchange.getRequestMethod()) {
                case "GET" -> handleGetRequest(exchange);
                case "POST" -> handlePostRequest(exchange);
                case "DELETE" -> handleDeleteRequest(exchange);
                default -> sendIncorrectMethod(exchange);
            }
        } catch (NumberFormatException e) {
            sendBadRequest(exchange, "Некорректно передан ID подзадачи");
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, INCORRECT_CONTENT);
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2) {
            sendJsonResponse(exchange, manager.getSubtasks());
        } else if (pathParts.length == 3) {
            SubTask subTask = manager.getSubtask(Integer.parseInt(pathParts[2]));
            if (subTask != null) {
                sendJsonResponse(exchange, subTask);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        try (InputStream input = exchange.getRequestBody();
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {

            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            SubTask subTask = gson.fromJson(reader, SubTask.class);
            if (subTask == null) {
                sendBadRequest(exchange, INCORRECT_CONTENT);
                return;
            }
            if (subTask.getEpicId() == 0) {
                sendBadRequest(exchange, "Для подзадачи должен быть указан epicId");
            } else if (pathParts.length == 2) {
                try {
                    int newId = manager.addNewSubtask(subTask.getEpicId(), subTask);
                    sendJsonResponse(exchange,
                            Map.of(STATUS_FIELD, TASK_CREATED, ID_FIELD, newId),
                            201);
                } catch (NoSuchElementException e) {
                    sendNotFound(exchange);
                } catch (IllegalArgumentException e) {
                    sendHasInteractions(exchange);
                }
            } else if (pathParts.length == 3) {
                int subTaskId = Integer.parseInt(pathParts[2]);
                subTask.setId(subTaskId);

                try {
                    manager.updateSubtask(subTask);
                    sendJsonResponse(exchange,
                            Map.of(STATUS_FIELD, TASK_UPDATED, ID_FIELD, subTaskId),
                            201);
                } catch (NoSuchElementException e) {
                    sendNotFound(exchange);
                } catch (IllegalArgumentException e) {
                    sendHasInteractions(exchange);
                }
            } else {
                sendHasInteractions(exchange);
            }
        }
    }


    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 3) {
            int id = Integer.parseInt(pathParts[2]);
            manager.deleteSubtask(id);
            sendJsonResponse(exchange, Map.of(STATUS_FIELD, TASK_DELETED, ID_FIELD, id));
        } else {
            sendNotFound(exchange);
        }
    }
}
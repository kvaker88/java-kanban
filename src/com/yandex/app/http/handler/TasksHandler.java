package com.yandex.app.http.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;

public class TasksHandler extends BaseHttpHandler implements HttpHandler {
    public TasksHandler(InMemoryTasksManager manager) {
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
            sendBadRequest(exchange, "Некорректно передан ID задачи");
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, INCORRECT_CONTENT);
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2) {
            sendJsonResponse(exchange, manager.getTasks());
        } else if (pathParts.length == 3) {
            Task task;
            try {
                task = manager.getTask(Integer.parseInt(pathParts[2]));
            } catch (NoSuchElementException e) {
                sendNotFound(exchange);
                return;
            }
            sendJsonResponse(exchange, task);
        } else {
            sendIncorrectMethod(exchange);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        try (InputStream input = exchange.getRequestBody();
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {

            String[] pathParts = exchange.getRequestURI().getPath().split("/");
            Task task = gson.fromJson(reader, Task.class);
            if (task == null) {
                sendBadRequest(exchange, INCORRECT_CONTENT);
                return;
            }

            if (pathParts.length == 2) {
                try {
                    int newId = manager.addNewTask(task);
                    sendJsonResponse(exchange,
                            Map.of(STATUS_FIELD, TASK_CREATED, ID_FIELD, newId),
                            201);
                } catch (IllegalArgumentException e) {
                    sendHasInteractions(exchange);
                }
            } else if (pathParts.length == 3) {
                int taskId = Integer.parseInt(pathParts[2]);
                task.setId(taskId);

                try {
                    manager.updateTask(task);
                    sendJsonResponse(exchange,
                            Map.of(STATUS_FIELD, TASK_UPDATED, ID_FIELD, taskId),
                            201);
                } catch (NoSuchElementException e) {
                        sendNotFound(exchange);
                } catch (IllegalArgumentException e) {
                    sendHasInteractions(exchange);
                }
            } else {
                sendIncorrectMethod(exchange);
            }
        }
    }

    private void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
            if (pathParts.length == 3) {
                try {
                int id = Integer.parseInt(pathParts[2]);
                manager.deleteTask(id);
                sendJsonResponse(exchange, Map.of(STATUS_FIELD, TASK_DELETED, ID_FIELD, id));
                } catch (NoSuchElementException e) {
                    sendNotFound(exchange);
                }
            } else {
                sendIncorrectMethod(exchange);
            }
    }
}
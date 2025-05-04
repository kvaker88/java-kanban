package com.yandex.app.http.handler;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.task.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(InMemoryTasksManager manager) {
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
            sendBadRequest(exchange, "Некорректно передан ID эпика");
        } catch (JsonSyntaxException e) {
            sendBadRequest(exchange, INCORRECT_CONTENT);
        } catch (IllegalArgumentException e) {
            sendHasInteractions(exchange);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        if (pathParts.length == 2) {
            sendJsonResponse(exchange, manager.getEpics());
            return;
        }

        Epic epic;
        try {
            epic = manager.getEpic(Integer.parseInt(pathParts[2]));
        } catch (NoSuchElementException e) {
            sendNotFound(exchange);
            return;
        }

        if (pathParts.length == 3) {
            sendJsonResponse(exchange, epic);
        } else if (pathParts.length == 4) {
            sendJsonResponse(exchange, epic.getSubtasks());
        } else {
            sendIncorrectMethod(exchange);
        }
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        try (InputStream input = exchange.getRequestBody();
        InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            String[] pathParts = exchange.getRequestURI().getPath().split("/");

            Epic epic = gson.fromJson(reader, Epic.class);

            if (epic == null) {
                sendBadRequest(exchange, INCORRECT_CONTENT);
                return;
            }

            if (pathParts.length == 2) {
                try {
                    int newId = manager.addNewEpic(epic);
                    sendJsonResponse(exchange,
                            Map.of(STATUS_FIELD, TASK_CREATED, ID_FIELD, newId),
                            201);
                } catch (IllegalArgumentException e) {
                    sendHasInteractions(exchange);
                }
            } else if (pathParts.length == 3) {
                int epicId = Integer.parseInt(pathParts[2]);
                epic.setId(epicId);
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
                manager.deleteEpic(id);
                sendJsonResponse(exchange, Map.of(STATUS_FIELD, TASK_DELETED, ID_FIELD, id));
            } catch (NoSuchElementException e) {
                sendNotFound(exchange);
            }
        } else {
            sendIncorrectMethod(exchange);
        }
    }
}
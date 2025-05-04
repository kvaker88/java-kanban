package com.yandex.app.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.http.adapter.DurationAdapter;
import com.yandex.app.http.adapter.LocalDateTimeAdapter;
import com.yandex.app.manager.InMemoryTasksManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class BaseHttpHandler {

    public static final String STATUS_FIELD = "status";
    public static final String ID_FIELD = "id";
    public static final String ERROR_FIELD = "error";

    public static final String TASK_UPDATED = "Задача обновлена";
    public static final String TASK_CREATED = "Задача создана";
    public static final String TASK_DELETED = "Задача удалена";
    public static final String TASK_NOT_FOUND = "Задача не найдена";
    public static final String TIME_CONFLICT = "Время выполнения пересекается с существующей задачей";
    public static final String INVALID_METHOD = "Использован некорректный метод при запросе";
    public static final String INCORRECT_CONTENT = "Некорректно переданы поля задачи";

    protected final InMemoryTasksManager manager;
    protected final Gson gson;

    public BaseHttpHandler(InMemoryTasksManager manager) {
        this.manager = manager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .setPrettyPrinting()
                .create();
    }

    protected void sendJsonResponse(HttpExchange exchange, String jsonBody, int statusCode) throws IOException {
        byte[] response = jsonBody.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        exchange.sendResponseHeaders(statusCode, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    protected void sendText(HttpExchange exchange, String text, int statusCode) throws IOException {
        sendJsonResponse(exchange,text, statusCode);
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, Map.of(ERROR_FIELD, TASK_NOT_FOUND), 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, Map.of(STATUS_FIELD, TIME_CONFLICT), 406);
    }

    protected void sendIncorrectMethod(HttpExchange exchange) throws IOException {
        sendJsonResponse(exchange, Map.of(STATUS_FIELD, INVALID_METHOD), 400);
    }

    protected void sendJsonResponse(HttpExchange exchange, Object data) throws IOException {
        sendText(exchange, gson.toJson(data), 200);
    }

    protected void sendJsonResponse(HttpExchange exchange, Object data, int statusCode) throws IOException {
        sendText(exchange, gson.toJson(data), statusCode);
    }

    protected void sendBadRequest(HttpExchange exchange, String message) throws IOException {
        sendText(exchange, gson.toJson(Map.of(ERROR_FIELD, message)), 400);
    }
}

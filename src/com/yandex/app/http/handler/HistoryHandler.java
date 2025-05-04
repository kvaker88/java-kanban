package com.yandex.app.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.yandex.app.manager.InMemoryTasksManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(InMemoryTasksManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equals(exchange.getRequestMethod())) {
            sendIncorrectMethod(exchange);
            return;
        }

        String historyJson = gson.toJson(manager.getHistory());
        sendText(exchange, historyJson, 200);
    }
}

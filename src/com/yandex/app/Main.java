package com.yandex.app;

import com.yandex.app.http.HttpTaskServer;
import com.yandex.app.manager.InMemoryTasksManager;

public class Main {
    public static void main(String[] args) {
        InMemoryTasksManager manager = new InMemoryTasksManager();
        HttpTaskServer httpTaskServer = new HttpTaskServer();

        httpTaskServer.start(manager);
        System.out.println("Сервер запущен, слушает порт 8080.");
    }
}
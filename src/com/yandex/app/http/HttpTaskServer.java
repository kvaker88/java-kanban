package com.yandex.app.http;

import com.sun.net.httpserver.HttpServer;
import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.http.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    HttpServer httpServer;

    public void start(InMemoryTasksManager manager) {
        try {
            httpServer = HttpServer.create();

            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TasksHandler(manager));
            httpServer.createContext("/subtasks", new SubTasksHandler(manager));
            httpServer.createContext("/epics", new EpicHandler(manager));
            httpServer.createContext("/history", new HistoryHandler(manager));
            httpServer.createContext("/prioritized", new PrioritizedHandler(manager));

            httpServer.start();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void stop() {
        httpServer.stop(0);
    }
}

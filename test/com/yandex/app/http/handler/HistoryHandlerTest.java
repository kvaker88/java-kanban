package com.yandex.app.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.DefaultTasksTest;
import com.yandex.app.http.HttpTaskServer;
import com.yandex.app.http.adapter.DurationAdapter;
import com.yandex.app.http.adapter.LocalDateTimeAdapter;
import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.task.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HistoryHandlerTest {
    DefaultTasksTest defaultTasksTest = new DefaultTasksTest();
    HttpTaskServer httpTaskServer = new HttpTaskServer();
    InMemoryTasksManager manager = new InMemoryTasksManager();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .setPrettyPrinting()
            .create();


    @BeforeEach
    void setUp() {
        manager.deleteTasks();
        manager.deleteSubtasks();
        manager.deleteEpics();
        httpTaskServer.start(manager);
    }

    @AfterEach
    void shutDown() {
        httpTaskServer.stop();
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
        Task task = new Task(
                "Тестовая задачи getHistory()",
                "Описание тестовой задачи getHistory()");
        int taskId = manager.addNewTask(task);
        manager.getTask(taskId); // Добавляем в историю

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/history"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Task> history = gson.fromJson(response.body(), taskListType);

        assertEquals(200, response.statusCode(), "Код ответа не 200 в getHistory()");
        assertEquals(1, history.size(), "Количество задач не совпадает в getHistory()");
        assertTrue(defaultTasksTest.equalsTask(task, history.getFirst()),
                "Задача в истории отличается в getHistory()");

        client.close();
    }

}

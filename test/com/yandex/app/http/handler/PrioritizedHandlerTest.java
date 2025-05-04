package com.yandex.app.http.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.DefaultTasksTest;
import com.yandex.app.http.HttpTaskServer;
import com.yandex.app.http.adapter.DurationAdapter;
import com.yandex.app.http.adapter.LocalDateTimeAdapter;
import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.task.Status;
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

class PrioritizedHandlerTest {
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
    void getPrioritizedTasks() throws IOException, InterruptedException {
        Task task = new Task(
                1,
                "Тестовая задача для getPrioritizedTasks()",
                "Описание тестовой задачи getPrioritizedTasks()",
                Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.now());

        manager.addNewTask(task);
        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/prioritized"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Task> prioritized = gson.fromJson(response.body(), taskListType);

        assertEquals(200, response.statusCode(), "Код ответа не 200 в getPrioritizedTasks()");
        assertEquals(1, prioritized.size(), "Количество задач не совпадает в getPrioritizedTasks()");
        assertTrue(defaultTasksTest.equalsTask(task, prioritized.getFirst()),
                "Задачи отличаются при сравнивании в getPrioritizedTasks()");

        client.close();
    }
}

package com.yandex.app.http.handler;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.DefaultTasksTest;
import com.yandex.app.http.HttpTaskServer;
import com.yandex.app.http.adapter.DurationAdapter;
import com.yandex.app.http.adapter.LocalDateTimeAdapter;
import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class EpicHandlerTest {
    DefaultTasksTest defaultTasksTest = new DefaultTasksTest();
    HttpTaskServer httpTaskServer = new HttpTaskServer();
    InMemoryTasksManager manager = new InMemoryTasksManager();
    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
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
    void getEpics() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Тестовый эпик getEpics()",
                "Описание для эпика getEpics()");

        manager.addNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/epics"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        Type epicListType = new TypeToken<List<Epic>>(){}.getType();
        List<Epic> epics = gson.fromJson(response.body(), epicListType);

        assertEquals(200, response.statusCode(), "Код ответа не 200 в getEpics()");
        assertEquals(1, epics.size(), "Количество эпиков отличается в getEpics()");
        assertTrue(defaultTasksTest.equalsEpic(epic, epics.getFirst()),
                "Эпики отличаются в getEpics()");

        client.close();
    }

    @Test
    void getEpicById() throws IOException, InterruptedException {
        Epic epic = new Epic("Test Epic", "Description");
        int id = manager.addNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/epics/" + id))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        Epic responseEpic = gson.fromJson(response.body(), Epic.class);

        assertEquals(200, response.statusCode(), "Код ответа не 200 в getEpicById()");
        assertEquals(id, responseEpic.getId(), "ID эпиков отличаются в getEpicById()");
        assertTrue(defaultTasksTest.equalsEpic(epic, responseEpic), "Эпики отличаются в getEpicById()");

        client.close();
    }

    @Test
    void getEpicSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Тестовый эпик getEpicSubtasks()",
                "Описание эпика getEpicSubtasks()");
        int epicId = manager.addNewEpic(epic);

        SubTask subTask = new SubTask(
                2,
                1,
                "Тестовая подзадача getEpicSubtasks()",
                "Описание подзадачи getEpicSubtasks()",
                Status.NEW
        );

        manager.addNewSubtask(1, subTask);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/epics/" + epicId + "/subtasks"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        Type subtaskListType = new TypeToken<List<SubTask>>(){}.getType();
        List<SubTask> subtasks = gson.fromJson(response.body(), subtaskListType);

        assertEquals(200, response.statusCode(), "Код ответа не 200 в getEpicSubtasks()");
        assertEquals(1, subtasks.size(), "Количество подзадач не совпадает в getEpicSubtasks()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask, subtasks.getFirst()),
                "Подзадачи отличаются в getEpicSubtasks()");

        client.close();
    }

    @Test
    void createEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(
                1,
                "Тестовый эпик createEpic()",
                "Описание эпика createEpic()");

        String taskJson = gson.toJson(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Epic epicsFromManager = manager.getEpic(1);

        assertEquals(201, response.statusCode(), "Код ответа не 201 в createEpic()");
        assertNotNull(epicsFromManager, "Эпики не возвращаются в createEpic()");
        assertTrue(defaultTasksTest.equalsEpic(epic, epicsFromManager),
                "Эпики отличаются в createEpic()");

        client.close();
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic(
                "Тестовый эпик deleteEpic()",
                "Описание эпика deleteEpic()");
        int id = manager.addNewEpic(epic);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/epics/" + id))
                        .DELETE()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode(), "Код ответа не 200 в deleteEpic()");
        assertThrows(NoSuchElementException.class, () -> {
            manager.getEpic(id);
        } , "Эпик не был удалён в deleteEpic()");

        client.close();
    }
}
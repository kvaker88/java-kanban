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

class SubTasksHandlerTest {
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
    void getSubTasks() throws IOException, InterruptedException {
        manager.addNewEpic(new Epic(
                "Эпик для getSubTasks()",
                "Описание эпика getSubTasks()"));

        SubTask subTask = new SubTask(
                2,
                1,
                "Подзадача для getSubTasks()",
                "Описание подзадачи getSubTasks()",
                Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.now());

        manager.addNewSubtask(1, subTask);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/subtasks"))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        Type subtaskListType = new TypeToken<List<SubTask>>(){}.getType();
        List<SubTask> subtasks = gson.fromJson(response.body(), subtaskListType);

        assertEquals(200, response.statusCode());
        assertEquals(1, subtasks.size());
        assertTrue(defaultTasksTest.equalsTask(subTask, subtasks.getFirst()));

        client.close();
    }

    @Test
    void getSubTaskById() throws IOException, InterruptedException {
        manager.addNewEpic(new Epic(
                "Эпик для getSubTasks()",
                "Описание эпика getSubTaskById()"));

        SubTask subTask = new SubTask(
                2,
                1,
                "Подзадача для getSubTaskById()",
                "Описание подзадачи getSubTaskById()",
                Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.now());

        int id = manager.addNewSubtask(1, subTask);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/subtasks/" + id))
                        .GET()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        SubTask responseSubTask = gson.fromJson(response.body(), SubTask.class);

        assertEquals(200, response.statusCode());
        assertEquals(id, responseSubTask.getId());
        assertTrue(defaultTasksTest.equalsTask(subTask, responseSubTask));

        client.close();
    }

    @Test
    void createSubTask() throws IOException, InterruptedException {
        manager.addNewEpic(new Epic(
                "Эпик для createSubTask()",
                "Описание эпика createSubTask()"));

        SubTask subTask = new SubTask(
                2,
                1,
                "Подзадача для createSubTask()",
                "Описание подзадачи createSubTask()",
                Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 30));

        String taskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<SubTask> tasksFromManager = manager.getSubtasks();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Подзадачи не возвращаются в createSubTask()");
        assertEquals(1, tasksFromManager.size(),
                "Некорректное количество подзадач в createSubTask()");
        assertTrue(defaultTasksTest.equalsSubTask(subTask, manager.getSubtask(2)),
                "Идентичные подзадачи отличаются при сравнивании в createSubTask()");

        client.close();
    }

    @Test
    void updateSubTask() throws IOException, InterruptedException {
        manager.addNewEpic(new Epic(
                "Эпик для updateSubTask()",
                "Описание эпика updateSubTask()"));

        SubTask subTask = new SubTask(
                2,
                1,
                "Подзадача для updateSubTask()",
                "Описание подзадачи updateSubTask()",
                Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.now());

        int id = manager.addNewSubtask(1, subTask);

        SubTask updated = new SubTask(
                id,
                subTask.getEpicId(),
                "Обновлённое название подзадачи для updateSubTask()",
                "Новое описание подзадачи updateSubTask()",
                Status.DONE,
                Duration.ofMinutes(30),
                LocalDateTime.now().plusHours(1));
        String json = gson.toJson(updated);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/subtasks/" + id))
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(201, response.statusCode());
        SubTask fromManager = manager.getSubtask(id);
        assertEquals("Обновлённое название подзадачи для updateSubTask()", fromManager.getName());
        assertEquals(Status.DONE, fromManager.getStatus());

        client.close();
    }

    @Test
    void deleteSubTask() throws IOException, InterruptedException {
        manager.addNewEpic(new Epic(
                "Эпик для deleteSubTask()",
                "Описание эпика deleteSubTask()"));

        SubTask subTask = new SubTask(
                2,
                1,
                "Подзадача для deleteSubTask()",
                "Описание подзадачи deleteSubTask()",
                Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.now());

        int id = manager.addNewSubtask(1, subTask);

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(
                HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/subtasks/" + id))
                        .DELETE()
                        .build(),
                HttpResponse.BodyHandlers.ofString()
        );

        assertEquals(200, response.statusCode(), "Код ответа не 200 в deleteSubTask()");
        assertThrows(NoSuchElementException.class, () -> {
            manager.getSubtask(id);
        }, "Задача не была удалена в deleteSubTask()");

        client.close();
    }
}

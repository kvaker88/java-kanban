package com.yandex.app.http.handler;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.yandex.app.DefaultTasksTest;
import com.yandex.app.http.HttpTaskServer;
import com.yandex.app.http.adapter.DurationAdapter;
import com.yandex.app.http.adapter.LocalDateTimeAdapter;
import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.task.Status;
import com.yandex.app.task.Task;
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

class TasksHandlerTest {
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
    void getTasks() throws IOException, InterruptedException {
        Task task = new Task(
                1,
                "Тестовая задача для getTasks()",
                "Описание задачи getTasks()",
                Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 30));

        manager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Type taskListType = new TypeToken<List<Task>>(){}.getType();
        List<Task> tasksFromResponse = gson.fromJson(response.body(), taskListType);

        assertEquals(200, response.statusCode());
        assertTrue(defaultTasksTest.equalsTask(task, tasksFromResponse.getFirst()),
                "Задача в менеджере отличается от отданной через API в getTasks()");

        client.close();
    }

    @Test
    void getTaskById() throws IOException, InterruptedException {
        Task task = new Task(
                1,
                "Тестовая задача для getTaskById()",
                "Описание задачи getTaskById()",
                Status.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.now());
        int taskId = manager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        Task taskFromResponse = gson.fromJson(response.body(), Task.class);
        assertEquals(taskId, taskFromResponse.getId(), "ID задач не совпадают getTaskById()");
        assertTrue(defaultTasksTest.equalsTask(task, taskFromResponse),
                "Содержимое задачи не совпадает в getTaskById()");

        client.close();
    }

    @Test
    void createTask() throws IOException, InterruptedException {
        Task task = new Task(
                1,
                "Тестовая задача для createTask()",
                "Описание задачи createTask()",
                Status.NEW,
                Duration.ofMinutes(15),
                LocalDateTime.of(1970, 1, 1, 0, 30));

        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromManager = manager.getTasks();

        assertEquals(201, response.statusCode());
        assertNotNull(tasksFromManager, "Задачи не возвращаются в createTask()");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач в createTask()");
        assertTrue(defaultTasksTest.equalsTask(task, manager.getTask(1)),
                "Идентичные задачи отличаются при сравнивании в createTask()");

        client.close();
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Task task = new Task(
                1,
                "Тестовая задача для updateTask()",
                "Описание задачи updateTask()",
                Status.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.now());
        int taskId = manager.addNewTask(task);

        Task updatedTask = new Task(
                taskId,
                "Обновленная задача для updateTask()",
                "Новое описание задачи updateTask()",
                Status.IN_PROGRESS,
                Duration.ofMinutes(10),
                LocalDateTime.now().plusHours(1));
        String updatedTaskJson = gson.toJson(updatedTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(updatedTaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Task taskFromManager = manager.getTask(taskId);

        assertTrue(defaultTasksTest.equalsTask(updatedTask, taskFromManager),
                "Содержимое задачи не обновилось в updateTask()");

        client.close();
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        Task task = new Task(
                1,
                "Тестовая задача для deleteTask()",
                "Описание задачи deleteTask()",
                Status.NEW,
                Duration.ofMinutes(5),
                LocalDateTime.now());
        int taskId = manager.addNewTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/" + taskId);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        assertThrows(NoSuchElementException.class, () -> {
            manager.getTask(taskId);
        } , "Задача не была удалена в deleteTask() ");
        assertTrue(manager.getTasks().isEmpty(), "Список задач должен быть пустым в deleteTask() ");

        client.close();
    }
}
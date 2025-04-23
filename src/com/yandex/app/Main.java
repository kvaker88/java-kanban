package com.yandex.app;

import com.yandex.app.manager.FileBackedTasksManager;
import com.yandex.app.manager.Managers;
import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        File file = null;
        try {
            file = File.createTempFile("save_", ".csv");
        } catch (IOException exception) {
            System.out.println(exception);
        }
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultTasksManager(file);

        System.out.println("Сохранились, загрузили пустой файл.");

        Task task = new Task(
                "Task1",
                "Description task1",
                Duration.ofMinutes(30),
                LocalDateTime.of(2025,4,22,17,30)
        );
        Epic epic = new Epic(
                "Epic2",
                "Description epic2"
        );
        SubTask subTask = new SubTask(
                3,
                2,
                "Sub Task2",
                "Description sub task3",
                Status.IN_PROGRESS,
                Duration.ofMinutes(30),
                LocalDateTime.of(2025,4,22,17,0)
        );

        SubTask subTask2 = new SubTask(
                4,
                2,
                "Sub Task4",
                "Description sub task4",
                Status.IN_PROGRESS,
                Duration.ofMinutes(30),
                LocalDateTime.of(2025,4,22,16,30)
        );

        task.setStatus(Status.DONE);
        subTask.setStatus(Status.DONE);

        fileBackedTasksManager.addNewTask(task);
        fileBackedTasksManager.addNewEpic(epic);
        fileBackedTasksManager.addNewSubtask(2, subTask);
        fileBackedTasksManager.addNewSubtask(2, subTask2);

        fileBackedTasksManager.updateSubtask(new SubTask(3,
                2,
                "Sub Task3",
                "Description sub task3",
                Status.DONE,
                Duration.ofMinutes(30),
                null
        ));

        System.out.println("Задачи - " + fileBackedTasksManager.getTasks());
        System.out.println("Эпики - " + fileBackedTasksManager.getEpics());
        System.out.println("Подзадачи - " + fileBackedTasksManager.getSubtasks());

        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile(file);
        System.out.println("Загрузились из файла.");

        System.out.println("Задачи - " + fileBackedTasksManager1.getTasks());
        System.out.println("Эпики - " + fileBackedTasksManager1.getEpics());
        System.out.println("Подзадачи - " + fileBackedTasksManager1.getSubtasks());
    }
}
package com.yandex.app;

import com.yandex.app.manager.FileBackedTasksManager;
import com.yandex.app.manager.Managers;
import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;

import java.io.File;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        // перемененная для последующей загрузки из файла
        final Path path = Path.of("C:\\Users\\tihon\\IdeaProjects\\java-kanban\\");
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultTasksManager();

        fileBackedTasksManager.loadFromFile(new File(path + "save.csv"));
        System.out.println("Сохранились, загрузили пустой файл ");

        // Далее создаём задачи по ТЗ через новый менеджер, автоматически сохраняем в файл

        Task task = new Task("Task1", "Description task1");
        task.setStatus(Status.DONE);

        Epic epic = new Epic("Epic2", "Description epic2");
        SubTask subTask = new SubTask(2, "Sub Task2", "Description sub task3");
        subTask.setStatus(Status.DONE);

        fileBackedTasksManager.addNewTask(task);
        fileBackedTasksManager.addNewEpic(epic);
        fileBackedTasksManager.addNewSubtask(2, subTask);

        fileBackedTasksManager.updateSubtask(new SubTask(3, 2, "Sub Task2",
                "Description sub task3", Status.DONE));

        // Проверяем какие задачи в программе, сверяем с содержимым файла

        FileBackedTasksManager fileBackedTasksManager1 = fileBackedTasksManager
                .loadFromFile(new File("C:\\Users\\tihon\\IdeaProjects\\java-kanban\\save.csv"));

        System.out.println("Задачи - " + fileBackedTasksManager1.getTasks());
        System.out.println("Эпики - " + fileBackedTasksManager1.getEpics());
        System.out.println("Подзадачи - " + fileBackedTasksManager1.getSubtasks());

        // Задачи из программы соответствуют содержимому файла
    }
}
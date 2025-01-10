package com.yandex.app;

import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.task.Epic;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;


public class Main {
    public static void main(String[] args) {
        InMemoryTasksManager tasksManager = new InMemoryTasksManager();

        tasksManager.addNewTask(new Task("Task#1", "TaskDescription#1"));
        tasksManager.addNewTask(new Task("Task#2", "TaskDescription#2"));

        tasksManager.addNewEpic(new Epic("Epic#3", "EpicDescription#3"));

        tasksManager.addNewSubtask(3, (new SubTask(3, "SubTask#4",
                "description#4")));
        tasksManager.addNewSubtask(3, (new SubTask(3, "SubTask#5",
                "SubTaskDescription#5")));
        tasksManager.addNewSubtask(3, (new SubTask(3, "SubTask#6",
                "SubTaskDescription#6")));

        tasksManager.addNewEpic(new Epic("Epic#7", "EpicDescription#7"));

        tasksManager.getSubtask(5);
        tasksManager.getTask(2);
        tasksManager.getTask(1);
        tasksManager.getEpic(7);
        tasksManager.getTask(1);

        System.out.println("-".repeat(40));
        System.out.println("Первый вывод 5, 2, 7, 1:");
        System.out.println(tasksManager.getHistory());
        System.out.println("-".repeat(40));

        tasksManager.getTask(2);
        tasksManager.getSubtask(5);
        tasksManager.getSubtask(4);
        tasksManager.getEpic(7);
        tasksManager.getTask(1);
        tasksManager.getEpic(3);
        tasksManager.getSubtask(5);

        System.out.println("Второй вывод 2, 4, 7, 1, 3, 5:");
        System.out.println(tasksManager.getHistory()); // Второй вывод. Задачи
        System.out.println("-".repeat(40));

        tasksManager.deleteTask(2);
        tasksManager.deleteEpic(3);

        System.out.println("Третий вывод 7, 1:");
        System.out.println(tasksManager.getHistory()); // Второй вывод. Задачи
        System.out.println("-".repeat(40));

        tasksManager.deleteTask(1);
        tasksManager.deleteEpic(7);

        System.out.println("Четвёртый пустой вывод:");
        System.out.println(tasksManager.getHistory()); // Второй вывод. Задачи
        System.out.println("-".repeat(40));
    }
}
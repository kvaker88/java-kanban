package com.yandex.app.history;

import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.task.Epic;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    InMemoryTasksManager tasksManager = new InMemoryTasksManager();

    @Test
    void addAndGetHistory() {
        tasksManager.addNewTask(new Task(
                "Task#1 addAndGetHistory",
                "addAndGetHistory#1 description"
        ));
        tasksManager.addNewTask(new Task(
                "Task#2 addAndGetHistory",
                "addAndGetHistory#2 description"
        ));
        tasksManager.addNewTask(new Task(
                "Task#3 addAndGetHistory",
                "addAndGetHistory#3 description"
        ));
        tasksManager.addNewTask(new Task(
                "Task#4 addAndGetHistory",
                "addAndGetHistory#4 description"
        ));
        tasksManager.addNewTask(new Task(
                "Task#5 addAndGetHistory",
                "addAndGetHistory#5 description"
        ));

        tasksManager.addNewEpic(new Epic(
                "Epic#6 addAndGetHistory",
                "addAndGetHistory#6 description"
        ));
        tasksManager.addNewEpic(new Epic(
                "Test#7 addAndGetHistory",
                "addAndGetHistory#7 description"
        ));
        tasksManager.addNewEpic(new Epic(
                "Test#8 addAndGetHistory",
                "addAndGetHistory#8 description"
        ));
        tasksManager.addNewEpic(new Epic(
                "Test#9 addAndGetHistory",
                "addAndGetHistory#9 description"
        ));
        tasksManager.addNewEpic(new Epic(
                "Test#10 addAndGetHistory",
                "addAndGetHistory#10 description"
        ));

        tasksManager.addNewSubtask(
                6, (new SubTask(
                        6,
                        "Test#11 addAndGetHistory",
                        "addAndGetHistory#11 description"
                )));
        tasksManager.addNewSubtask(
                7, (new SubTask(
                        7,
                        "Test#12 addAndGetHistory",
                        "addAndGetHistory#12 description"
                )));
        tasksManager.addNewSubtask(
                8, (new SubTask(
                        8,
                        "Test#13 addAndGetHistory",
                        "addAndGetHistory#13 description"
                )));
        tasksManager.addNewSubtask(
                9, (new SubTask(
                        9,
                        "Test#14 addAndGetHistory",
                        "addAndGetHistory#14 description"
                )));
        tasksManager.addNewSubtask(
                10, (new SubTask(
                        10,
                        "Test#15 addAndGetHistory",
                        "addAndGetHistory#15 description"
                )));

        final List<Task> history1 = tasksManager.getHistory();
        assertEquals(0, history1.size());

        tasksManager.getTask(2);
        tasksManager.getTask(3);
        tasksManager.getEpic(7);
        tasksManager.getEpic(9);
        tasksManager.getSubtask(11);
        tasksManager.getSubtask(11);

        final List<Task> history2 = tasksManager.getHistory();
        assertEquals(5, history2.size());

        tasksManager.getTask(1);
        tasksManager.getTask(4);
        tasksManager.getEpic(6);
        tasksManager.getEpic(8);

        final List<Task> history3 = tasksManager.getHistory();
        assertEquals(9, history3.size());

        tasksManager.getSubtask(13);
        tasksManager.getSubtask(14);

        final List<Task> history4 = tasksManager.getHistory();
        assertEquals(11, history4.size());

        tasksManager.getTask(5);
        tasksManager.getEpic(10);
        tasksManager.getSubtask(11);
        tasksManager.getSubtask(12);
        tasksManager.getSubtask(15);

        final List<Task> history5 = tasksManager.getHistory();
        assertEquals(15, history5.size());

        tasksManager.getTask(5);
        tasksManager.getTask(1);
        tasksManager.getTask(2);
        tasksManager.getTask(3);
        tasksManager.getEpic(10);
        tasksManager.getSubtask(15);
        tasksManager.getSubtask(11);
        tasksManager.getSubtask(12);

        assertEquals(15, history5.size());
    }

    @Test
    void removeHistory() {

        Task task1 = new Task(
                "Task#1 removeHistory",
                "removeHistory Description#1"
        );

        Task task2 = new Task(
                "Task#2 removeHistory",
                "removeHistory Description#2"
        );

        Task task3 = new Task(
                "Test#3 removeHistory",
                "removeHistory Description#3"
        );

        Task task4 = new Task(
                "Test#4 removeHistory",
                "removeHistory Description#4"
        );

        Task task5 = new Task(
                "Test#5 removeHistory",
                "removeHistory Description#5"
        );

        Task task6 = new Task(
                "Test#6 removeHistory",
                "removeHistory Description#6"
        );

        Task task7 = new Task(
                "Test#7 removeHistory",
                "removeHistory Description#7"
        );

        Task task8 = new Task(
                "Test#8 removeHistory",
                "removeHistory Description#8"
        );

        Task task9 = new Task(
                "Test#9 removeHistory",
                "removeHistory Description#9"
        );

        Task task10 = new Task(
                "Test#10 removeHistory",
                "removeHistory Description#10"
        );

        tasksManager.addNewTask(task1);
        tasksManager.addNewTask(task2);
        tasksManager.addNewTask(task3);
        tasksManager.addNewTask(task4);
        tasksManager.addNewTask(task5);
        tasksManager.addNewTask(task6);
        tasksManager.addNewTask(task7);
        tasksManager.addNewTask(task8);
        tasksManager.addNewTask(task9);
        tasksManager.addNewTask(task10);

        tasksManager.getTask(1);
        tasksManager.getTask(2);
        tasksManager.getTask(3);
        tasksManager.getTask(4);
        tasksManager.getTask(5);
        tasksManager.getTask(6);
        tasksManager.getTask(7);
        tasksManager.getTask(8);
        tasksManager.getTask(9);
        tasksManager.getTask(10);

        tasksManager.deleteTask(1);
        tasksManager.deleteTask(5);
        tasksManager.deleteTask(10);

        final List<Task> history = tasksManager.getHistory();
        final List<Task> taskForTest = tasksManager.getTasks();

        String result = tasksManager.getTasks().toString();
        String correctOrder =
                "[" +
                task2 + ", " +
                task3 + ", " +
                task4 + ", " +
                task6 + ", " +
                task7 + ", " +
                task8 + ", " +
                task9 + "]";

        assertEquals(7, history.size());
        assertEquals(taskForTest, tasksManager.getHistory(), "Задача не совпадают.");
        assertEquals(correctOrder, result, "Задача не совпадают.");
    }
}
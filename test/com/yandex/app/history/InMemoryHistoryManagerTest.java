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
    void addAndGetHistory() { // Проверка работы истории и корректность добавления задач, которые уже есть в истории
        tasksManager.addNewTask(new Task("Task#1 deleteEpics", "deleteEpics#1 description"));
        tasksManager.addNewTask(new Task("Task#2 deleteEpics", "deleteEpics#2 description"));
        tasksManager.addNewTask(new Task("Task#3 deleteEpics", "deleteEpics#3 description"));
        tasksManager.addNewTask(new Task("Task#4 deleteEpics", "deleteEpics#4 description"));
        tasksManager.addNewTask(new Task("Task#5 deleteEpics", "deleteEpics#5 description"));

        tasksManager.addNewEpic(new Epic("Epic#6 deleteEpics", "deleteEpics#6 description"));
        tasksManager.addNewEpic(new Epic("Test#7 deleteEpics", "deleteEpics#7 description"));
        tasksManager.addNewEpic(new Epic("Test#8 deleteEpics", "deleteEpics#8 description"));
        tasksManager.addNewEpic(new Epic("Test#9 deleteEpics", "deleteEpics#9 description"));
        tasksManager.addNewEpic(new Epic("Test#10 deleteEpics", "deleteEpics#10 description"));

        tasksManager.addNewSubtask(6, (new SubTask(6, "Test#11 deleteEpics",
                "deleteEpics#11 description")));
        tasksManager.addNewSubtask(7, (new SubTask(7, "Test#12 deleteEpics",
                "deleteEpics#12 description")));
        tasksManager.addNewSubtask(8, (new SubTask(8, "Test#13 deleteEpics",
                "deleteEpics#13 description")));
        tasksManager.addNewSubtask(9, (new SubTask(9, "Test#14 deleteEpics",
                "deleteEpics#14 description")));
        tasksManager.addNewSubtask(10, (new SubTask(10, "Test#15 deleteEpics",
                "deleteEpics#15 description")));

        final List<Task> history1 = tasksManager.getHistory();
        assertEquals(0, history1.size());

        tasksManager.getTask(2);
        tasksManager.getTask(3);
        tasksManager.getEpic(7);
        tasksManager.getEpic(9);
        tasksManager.getSubtask(11);
        tasksManager.getSubtask(15);

        final List<Task> history2 = tasksManager.getHistory();
        assertEquals(6, history2.size());

        tasksManager.getTask(1);
        tasksManager.getTask(4);
        tasksManager.getEpic(6);
        tasksManager.getEpic(8);

        final List<Task> history3 = tasksManager.getHistory();
        assertEquals(10, history3.size());

        tasksManager.getSubtask(13);
        tasksManager.getSubtask(14);

        final List<Task> history4 = tasksManager.getHistory();
        assertEquals(12, history4.size()); // Убрали лимиты, теперь задач больше 10

        tasksManager.getTask(5);
        tasksManager.getEpic(10);
        tasksManager.getSubtask(11);
        tasksManager.getSubtask(12);
        tasksManager.getSubtask(15);

        final List<Task> history5 = tasksManager.getHistory();
        assertEquals(15, history5.size());

        // Повторное добавление тех же задач, в другом порядке, для проверки отсутствия дублей
        tasksManager.getTask(5);
        tasksManager.getEpic(10);
        tasksManager.getSubtask(15);
        tasksManager.getSubtask(11);
        tasksManager.getSubtask(12);

        assertEquals(15, history5.size());
    }

    @Test
    void removeHistory() { // Проверка работы метода InMemoryHistoryManager.remove()
        tasksManager.addNewTask(new Task("Task#1", "TaskDescription#1"));
        tasksManager.addNewTask(new Task("Task#2", "TaskDescription#2"));
        Task task3 = new Task("Test#3", "TaskDescription#3");
        tasksManager.addNewTask(task3);

        tasksManager.getTask(1);
        tasksManager.getTask(2);
        tasksManager.getTask(3);

        tasksManager.deleteTask(1);
        tasksManager.deleteTask(2);

        final List<Task> history = tasksManager.getHistory();
        final List<Task> taskForTest = tasksManager.getTasks();

        assertEquals(1, history.size());
        assertEquals(taskForTest, tasksManager.getHistory(), "Задача не совпадают.");
    }
}
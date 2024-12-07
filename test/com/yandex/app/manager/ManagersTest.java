package com.yandex.app.manager;

import com.yandex.app.history.HistoryManager;
import com.yandex.app.task.Epic;
import com.yandex.app.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManagersTest {

    @Test
    void ReturnTaskManager() {
        TasksManager taskManager = Managers.getDefault();
        Assertions.assertNotNull(taskManager, "Метод getDefault() должен возвращать TaskManager.");
    }

    @Test
    void ReturnHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Assertions.assertNotNull(historyManager, "Метод getDefaultHistory() должен возвращать HistoryManager.");
    }

    @Test
    void taskEqualityById() {
        TasksManager taskManager1 = Managers.getDefault();
        TasksManager taskManager2 = Managers.getDefault();

        Task task1 = new Task("Test taskEqualityById","Test taskEqualityById description");
        Task task2 = new Task("Test taskEqualityById","Test taskEqualityById description");

        taskManager1.addNewTask(task1);
        taskManager2.addNewTask(task2);

        Assertions.assertEquals(task1, task2);
    }

    @Test
    void epicEqualityById() {
        TasksManager taskManager1 = Managers.getDefault();
        TasksManager taskManager2 = Managers.getDefault();

        Epic epic1 = new Epic("Test epicEqualityById","Test epicEqualityById description");
        Epic epic2 = new Epic("Test epicEqualityById","Test epicEqualityById description");

        taskManager1.addNewEpic(epic1);
        taskManager2.addNewEpic(epic2);

        Assertions.assertEquals(epic1, epic2);
    }
}
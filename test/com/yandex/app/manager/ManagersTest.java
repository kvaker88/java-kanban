package com.yandex.app.manager;

import com.yandex.app.history.HistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManagersTest {

    @Test
    void returnTaskManager() {
        TasksManager tasksManager = Managers.getDefault();
        Assertions.assertNotNull(tasksManager, "Метод getDefault() должен возвращать TaskManager.");
    }

    @Test
    void returnHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Assertions.assertNotNull(historyManager, "Метод getDefaultHistory() должен возвращать HistoryManager.");
    }
}
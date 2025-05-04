package com.yandex.app.manager;

import com.yandex.app.history.HistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class ManagersTest {

    @Test
    void returnTaskManager() {
        TasksManager tasksManager = Managers.getDefault();
        Assertions.assertNotNull(tasksManager, "Метод getDefault() не возвращает TaskManager");
    }

    @Test
    void returnHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Assertions.assertNotNull(historyManager, "Метод getDefaultHistory() не возвращает HistoryManager");
    }

    @Test
    void returnFileTasksManager() throws IOException {
        FileBackedTasksManager fileBackedTasksManager = Managers.getDefaultTasksManager(
                File.createTempFile("prak", "csv"));
        Assertions.assertNotNull(fileBackedTasksManager,
                "Метод getDefaultTasksManager() не возвращает FileBackedTasksManager");
    }
}
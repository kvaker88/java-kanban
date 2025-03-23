package com.yandex.app.manager;

import com.yandex.app.history.HistoryManager;
import com.yandex.app.history.InMemoryHistoryManager;

public class Managers {

    public static TasksManager getDefault() {
        return new InMemoryTasksManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getDefaultTasksManager() {
        return new FileBackedTasksManager();
    }
}

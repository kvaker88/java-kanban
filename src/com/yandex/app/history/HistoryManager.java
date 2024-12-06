package com.yandex.app.history;

import com.yandex.app.task.Task;

import java.util.List;

public interface HistoryManager {
    void addHistory(Task task);
    List<Task> getHistory();
}

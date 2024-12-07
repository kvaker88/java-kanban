package com.yandex.app.history;

import com.yandex.app.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static final int HISTORY_LIMIT = 10;
    private final List<Task> history = new ArrayList<>(); // История просмотров задач

    @Override
    public void addHistory(Task task) {
        if (task != null) {
            if (history.size() == HISTORY_LIMIT) {
                history.removeFirst();
            }
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}

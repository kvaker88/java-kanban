package com.yandex.app.history;

import com.yandex.app.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private static List<Task> history = new ArrayList<>(); // История просмотров задач

    @Override
    public void addHistory(Task task) {
        if (!history.contains(task)) {
            if (history.size() == 10) {
                history.removeFirst();
            }
            history.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}

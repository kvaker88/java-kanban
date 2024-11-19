package com.yandex.app.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// класс для выполнения действий с Эпиками
public class Epic extends Task {
    private final List<Task> subTasks = new ArrayList<>(); // создаём список подзадач

    // метод для создания Эпика
    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    // метод для добавления подзадач
    public void addSubTask(SubTask subTask) {
        subTasks.add(subTask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subTasks, epic.subTasks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subTasks);
    }

}
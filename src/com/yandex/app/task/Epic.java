package com.yandex.app.task;

import java.util.ArrayList;
import java.util.List;

// класс для выполнения действий с Эпиками
public class Epic extends Task {
    private final List<SubTask> subtasks = new ArrayList<>(); // создаём список подзадач

    // метод для создания Эпика
    public Epic(int id, String name, String description) {
        super(name, description);
        this.id = id;
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    // метод для добавления подзадач
    public void addSubTask(SubTask subTask) {
        subtasks.add(subTask);
    }

    public void removeSubTask(int subTaskid) {
        for (SubTask subTask : this.subtasks) {
            if (subTask.getId() == subTaskid) {
                this.subtasks.remove(subTask);
                break;
            }
        }
    }

    public void removeAllSubTask() {
        subtasks.clear();
    }

    public List<SubTask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Эпик {" +
                "ID:" + id +
                ", Название: " + name +
                ", Описание: " + description +
                ", Статус:" + status + "}";
    }
}
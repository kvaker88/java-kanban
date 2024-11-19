package com.yandex.app.task;

import com.yandex.app.manager.TasksManager;

import java.util.Objects;

// класс для выполнения действий с подзадачами
public class SubTask extends Task {
    public int epicId; // у каждой подзадачи есть свой эпик, к которому он привязан


    // метод для создания подзадачи
    public SubTask(int id, int epicId, String name, String description) {
        super(id, name, description);
        this.epicId = epicId; // присваиваем подзадаче эпик
        TasksManager.getEpic(epicId).addSubTask(this); // добавляем подзадачу в список подзадач у Эпика
    }

    // конструктор для внесения изменений в уже существующую подзадачу
    public SubTask(int id, String name, String description, Status stat) {
        super(id, name, description, stat);
    }

    public int getEpicId() { // метод для получения ID эпика подзадачи
        return epicId;
    }

    @Override
    public String toString() {
        return "Задача {" +
                "ID:" + super.getId() +
                ", Epic ID: " + epicId +
                ", Название: " + super.getName() +
                ", Описание: " + super.getDescription() +
                ", Статус:" + super.getStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubTask subTask = (SubTask) o;
        return epicId == subTask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(epicId);
    }

}


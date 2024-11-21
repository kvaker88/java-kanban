package com.yandex.app.task;

import java.util.Objects;

// класс для выполнения действий с обычными задачами
public class Task {
    protected int id; // айди
    protected String name; // имя
    protected String description; // описание
    protected Status status = Status.NEW;

    // конструтор для изменения существующей задачи, без статуса
    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    // конструтор для изменения существующей задачи, включая статус
    public Task(int id, String name, String description, Status stat) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = stat;
    }

    public Task(String name, String description) { // конструктор для создания новой задачи
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "Задача {" +
                "ID:" + id +
                ", Название: " + name +
                ", Описание: " + description +
                ", Статус:" + status + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

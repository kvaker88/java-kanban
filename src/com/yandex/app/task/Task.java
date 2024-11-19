package com.yandex.app.task;

import java.util.Objects;

// класс для выполнения действий с обычными задачами
public class Task {
    private int id; // айди
    private String name; // имя
    private String description; // описание
    private Status status = Status.NEW;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Task(int id, String name, String description) { // конструктор для создания новой задачи
        this.name = name;
        this.id = id;
        this.description = description;
    }

    // конструтор для изменения существующей задачи
    public Task(int id, String name, String description, Status stat) {
        this.name = name;
        this.id = id;
        this.description = description;
        setStatus(stat);
    }

    @Override
    public String toString() {
        return "Задача {" +
                "ID:" + id +
                ", Название: " + name +
                ", Описание: " + description +
                ", Статус:" + status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name) &&
                Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status);
    }

}
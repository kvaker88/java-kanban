package com.yandex.app.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    protected int id;
    protected String name;
    protected String description;
    protected Status status = Status.NEW;
    protected Type type = Type.TASK;
    protected Duration duration = Duration.ofMinutes(0);
    protected LocalDateTime startTime = null;

    public Task(
            String name,
            String description
    ) {
        this.name = name;
        this.description = description;
    }

    public Task(
            int id,
            String name,
            String description
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Task(
            int id,
            String name,
            String description,
            Status status
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(
            String name,
            String description,
            Duration duration,
            LocalDateTime startTime
    ) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(
            int id,
            String name,
            String description,
            Status status,
            Duration duration,
            LocalDateTime startTime
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

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
        return status;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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

    @Override
    public String toString() {
        String result = "";
        if (startTime != null) {
            result = "Задача {" +
                    "ID:" + id +
                    ", Название: " + name +
                    ", Описание: " + description +
                    ", Статус:" + status +
                    ", Дата и время начала: " + startTime.format(DATE_TIME_FORMATTER) +
                    ", Время выполнения: " + duration + "}";
        } else {
            result = "Задача {" +
                    "ID:" + id +
                    ", Название: " + name +
                    ", Описание: " + description +
                    ", Статус:" + status +
                    ", Дата и время начал: Не указаны" +
                    ", Время выполнения: Не указано}";
        }
        return result;
    }

    public String toStringToFile() { // метод на подобии toString, только для сохранения задачи в файл
        return id + "," +
                type + "," +
                name + "," +
                status + "," +
                description + "," +
                startTime + "," +
                duration + "\n";
    }
}

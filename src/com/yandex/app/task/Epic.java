package com.yandex.app.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<SubTask> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(
            int id,
            String name,
            String description
    ) {
        super(id, name, description);
        type = Type.EPIC;
    }

    public Epic(
            String name,
            String description
    ) {
        super(name, description);
        type = Type.EPIC;
    }

    public Epic(
            int id,
            String name,
            String description,
            Duration duration,
            LocalDateTime startTime
    ) {
        super(name, description, duration, startTime);
        this.id = id;
        type = Type.EPIC;
    }

    public Epic(
            String name,
            String description,
            Duration duration,
            LocalDateTime startTime
    ) {
        super(name, description, duration, startTime);
        type = Type.EPIC;
    }

    public Epic(
            int id,
            String name,
            String description,
            Status status
    ) {
        super(id, name, description, status);
        type = Type.EPIC;
    }

    public Epic(
            int id,
            String name,
            String description,
            Status status,
            Duration duration,
            LocalDateTime startTime
    ) {
        super(id, name, description, status, duration, startTime);
        type = Type.EPIC;
    }


    public void addSubTask(SubTask subTask) {
        subtasks.add(subTask);
    }

    public void removeSubTask(int subTaskId) {
        for (SubTask subTask : this.subtasks) {
            if (subTask.getId() == subTaskId) {
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

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        String result = "";
        if (startTime != null) {
            result = "Эпик {" +
                    "ID:" + id +
                    ", Название: " + name +
                    ", Описание: " + description +
                    ", Статус:" + status +
                    ", Дата и время начала: " + startTime.format(DATE_TIME_FORMATTER) +
                    ", Время выполнения: " + duration + "}";
        } else {
            result = "Эпик {" +
                    "ID:" + id +
                    ", Название: " + name +
                    ", Описание: " + description +
                    ", Статус:" + status +
                    ", Дата и время начал: Не указаны" +
                    ", Время выполнения: Не указано}";
        }
        return result;
    }

    @Override
    public String toStringToFile() { // метод на подобии toString, только для сохранения задачи в файл
        return id + "," +
                type + "," +
                name + "," +
                status + "," +
                description + "," +
                "" + "," +
                startTime + "," +
                duration + "\n";
    }
}
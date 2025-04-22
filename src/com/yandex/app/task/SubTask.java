package com.yandex.app.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private final int epicId;
    private Type type = Type.SUBTASK;
    private LocalDateTime startTime;

    public SubTask(
            int epicId,
            String name,
            String description
    ) {
        super(name, description);
        this.epicId = epicId;
    }

    public SubTask(
            int id,
            int epicId,
            String name,
            String description,
            Status status
    ) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public SubTask(
            int id,
            int epicId,
            String name,
            String description,
            Status status,
            Duration duration,
            LocalDateTime startTime
    ) {
        super(id, name, description, status, duration, startTime);
        this.startTime = startTime;
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String result = "";
        if (startTime != null) {
            result = "Подзадача {" +
                    "ID:" + id
                    + ", ID Эпика: " + epicId
                    + ", Название: " + name
                    + ", Описание: " + description
                    + ", Статус:" + status
                    + ", Дата и время начала: " + startTime.format(DATE_TIME_FORMATTER)
                    + ", Время выполнения: " + duration + "}";
        } else {
            result = "Подзадача {" +
                    "ID:" + id
                    + ", ID Эпика: " + epicId
                    + ", Название: " + name
                    + ", Описание: " + description
                    + ", Статус:" + status +
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
                epicId + "," +
                startTime + "," +
                duration + "\n";
    }
}
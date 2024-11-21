package com.yandex.app.task;

// класс для выполнения действий с подзадачами
public class SubTask extends Task {
    private int epicId; // у каждой подзадачи есть свой эпик, к которому он привязан

    // конструктор для создания подзадачи
    public SubTask(int epicId, String name, String description) {
        super(name, description);
        this.epicId = epicId; // присваиваем подзадаче эпик
    }

    // конструктор для внесения изменений в уже существующую подзадачу
    public SubTask(int id, String name, String description, Status stat) {
        super(id, name, description, stat);
    }

    public void setEpicId(int epicId) { // метод для получения ID эпика подзадачи
        this.epicId = epicId;
    }

    public int getEpicId() { // метод для получения ID эпика подзадачи
        return epicId;
    }

    @Override
    public String toString() {
        return "Подзадача {" +
                "ID:" + id +
                ", ID Эпика: " + epicId +
                ", Название: " + name +
                ", Описание: " + description +
                ", Статус:" + status + "}";
    }
}
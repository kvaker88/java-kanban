package com.yandex.app.history;

import com.yandex.app.task.Task;

public class Node {
    public Task task;
    private Node next;
    private Node prev;

    public Node(Node prev, Task task, Node next){
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

    // Методы ниже нужны для упрощения взаимодействия в методе InMemoryHistoryManager.remove(id)
    public Node getNext() {
        return next;
    }

    public Node getPrev() {
        return prev;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }
}
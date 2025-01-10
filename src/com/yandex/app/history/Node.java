package com.yandex.app.history;

import com.yandex.app.task.Task;

public class Node<N, P> {
    public Task task;
    public N next;
    public P prev;

    public Node(P prev, Task task, N next){
        this.task = task;
        this.next = next;
        this.prev = prev;
    }

    // Методы ниже нужны для упрощения взаимодействия в методе InMemoryHistoryManager.remove(id)
    public Node getNext() {
        return (Node) next;
    }

    public Node getPrev() {
        return (Node) prev;
    }

}

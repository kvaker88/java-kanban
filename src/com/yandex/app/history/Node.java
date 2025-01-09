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

    @Override
    public String toString() {
        return task.toString();
    }
}

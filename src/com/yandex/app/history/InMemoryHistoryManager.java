package com.yandex.app.history;

import com.yandex.app.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private Map<Integer, Node> history = new HashMap<>(); // История просмотров задач
    private Node tail;
    private Node head;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.containsKey(task.getId())) {
            remove(task.getId()); // Удаляем существующую задачу
        }

        Node oldTail = tail;
        Node newNode = new Node<>(tail, task, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        history.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        Node log = head;

        List<Task> historyList = new ArrayList<>();
        while (log != null) {
            historyList.add(log.task);
            log = (Node) log.next;
        }
        return historyList;
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = history.get(id);
        if (nodeToRemove == null) {
            return;
        }
        if (nodeToRemove.prev != null) {
            nodeToRemove.getPrev().next = nodeToRemove.next;
        } else {
            head = (Node) nodeToRemove.next;
        }
        if (nodeToRemove.next != null) {
            nodeToRemove.getNext().prev = nodeToRemove.prev;
        } else {
            tail = (Node) nodeToRemove.prev;
        }
        history.remove(id);
    }
}

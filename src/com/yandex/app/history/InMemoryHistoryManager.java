package com.yandex.app.history;

import com.yandex.app.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> history = new HashMap<>();
    private Node tail;
    private Node head;

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }

        Node oldTail = tail;
        Node newNode = new Node(tail, task, null);
        tail = newNode;

        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
        history.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        Node currentNode = head;

        List<Task> historyList = new ArrayList<>();
        while (currentNode != null) {
            historyList.add(currentNode.task);
            currentNode = currentNode.getNext();
        }
        return historyList;
    }

    @Override
    public void remove(int id) {
        Node nodeToRemove = history.get(id);
        if (nodeToRemove == null) {
            return;
        }
        if (nodeToRemove.getPrev() != null) {
            nodeToRemove
                    .getPrev()
                    .setNext(nodeToRemove.getNext());
        } else {
            head = nodeToRemove.getNext();
        }
        if (nodeToRemove.getNext() != null) {
            nodeToRemove
                    .getNext()
                    .setPrev(nodeToRemove.getPrev());
        } else {
            tail = nodeToRemove.getPrev();
        }
        history.remove(id);
    }
}

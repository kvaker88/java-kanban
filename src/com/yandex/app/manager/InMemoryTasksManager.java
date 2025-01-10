package com.yandex.app.manager;

import com.yandex.app.history.*;
import com.yandex.app.task.*;

import java.util.*;

// класс с менеджером задач для взаимодействия с задачами разных типов
public class InMemoryTasksManager implements TasksManager {

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private final Map<Integer, Task> tasks = new HashMap<>(); // Обычные задачи
    private final Map<Integer, Epic> epics = new HashMap<>(); // Эпики
    private final Map<Integer, SubTask> subtasks = new HashMap<>(); // Подзадачи

    private int id = 1; // счётчик ID задач

    @Override
    public int addNewTask(Task task) {
        task.setId(id);
        tasks.put(id++, task); // Добавляем задачу в Map
        return task.getId(); // Возвращаем ID добавленной задачи
    }

    @Override
    public int addNewEpic(Epic epic) { // метод для добавления эпика
        epic.setId(id);
        epics.put(id++, epic); // Добавляем задачу в Map
        return epic.getId(); // Возвращаем ID добавленной задачи
    }

    @Override
    public int addNewSubtask(int epicId, SubTask subTask) { // метод для добавления подзадачи
        if (!epics.containsKey(epicId)) { // Проверяем, существует ли Эпик
            System.out.println("Эписка с указанным ID нет.");
            return -1;
        }
        subTask.setId(id++);
        epics.get(epicId).addSubTask(subTask);
        subtasks.put(subTask.getId(), subTask); // Добавляем задачу в Map
        updateEpicStatus(epics.get(epicId));
        return subTask.getId(); // Возвращаем ID добавленной задачи
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubtasks() { // метод для получения подзадач
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() { // метод для получения эпиков
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId); // Получаем эпик по ID
        if (epic == null) {
            return new ArrayList<>(); // Эпик не найден, возвращаем пустой список
        } else {
            return epic.getSubtasks(); // Возвращаем список подзадач
        }
    }

    @Override
    public Epic getEpic(int id) { // метод для получения информации по отдельному Эпику по ID
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            System.out.println("Эпика с указанным ID нет в списке.");
            return null;
        }
    }

    @Override
    public Task getTask(int id) { // метод для получения информации по отдельной подзадаче по ID
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            System.out.println("Задачи с указанным ID нет в списке.");
            return null;
        }
    }

    @Override
    public SubTask getSubtask(int id) { // метод для получения информации по отдельной подзадаче по ID
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            System.out.println("Подзадачи с указанным ID нет в списке.");
            return null;
        }
    }

    // метод для обновления информации в задаче по ID
    @Override
    public void updateTask(Task task) { // метод для обновления информации обычных задач
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задачи с таким ID нет в списке.");
        }
    }

    // метод для обновления информации в Эпике по ID
    @Override
    public void updateEpic(Epic epic) { // метод для обновления информации Эпиков
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            oldEpic.setDescription(epic.getDescription());
            oldEpic.setName(epic.getName());
        } else {
            System.out.println("Эпика с таким ID нет в списке.");
        }
    }

    // метод для обновления информации в подзадаче по ID
    @Override
    public void updateSubtask(SubTask subTask) { // метод для обновления информации подзадач
        if (subtasks.containsKey(subTask.getId())) {
            Epic epic = epics.get(subtasks
                    .get(subTask.getId())
                    .getEpicId());
            epic.removeSubTask(subTask.getId());
            epic.addSubTask(subTask);

            updateEpicStatus(epic);
            subtasks.put(subTask.getId(), subTask);
        } else {
            System.out.println("Подзадачи с таким ID нет в списке.");
        }
    }

    @Override
    public void deleteTask(int id) { // метод для удаления задачи по ID
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.remove(id);
        } else {
            System.out.println("Задача с указанным ID не найдена.");
        }
    }

    @Override
    public void deleteEpic(int id) { // метод для удаления эпика и его подзадач по ID
        if (epics.containsKey(id)) {
            List<SubTask> subTasks = getEpicSubtasks(id);
            for (SubTask subTask : subTasks) {
                subtasks.remove(subTask.getId());
                historyManager.remove(subTask.getId());
            }
            historyManager.remove(id);
            epics.remove(id);
        } else {
            System.out.println("Эпик с указанным ID не найден.");
        }
    }

    @Override
    public void deleteSubtask(int id) { // метод для удаления подзадачи по ID
        if (subtasks.containsKey(id)) {
            SubTask subTask = subtasks.get(id);
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubTask(subTask.getId());
            updateEpicStatus(epic);
            historyManager.remove(id);
            subtasks.remove(id);
        } else {
            System.out.println("Подзадача с указанным ID не найден.");
        }
    }

    @Override
    public void deleteTasks() { // метод для удаления всех обычных задач
        List<Integer> tasksId = new ArrayList<>(tasks.keySet());
        for (Integer id : tasksId) {
            removeHistory(id);
            tasks.remove(id);
        }
    }

    @Override
    public void deleteSubtasks() { // метод для удаления всех подзадач
        List<Integer> subTasksId = new ArrayList<>(subtasks.keySet());
        for (Integer id : subTasksId) {
            removeHistory(id);
            subtasks.remove(id);
        }
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.removeAllSubTask();
        }
    }

    @Override
    public void deleteEpics() { // метод для удаления всех Эпиков и подзадач
        List<Integer> subTasksId = new ArrayList<>(subtasks.keySet());
        List<Integer> epicsId = new ArrayList<>(epics.keySet());

        for (Integer id : subTasksId) {
            removeHistory(id);
            subtasks.remove(id);
        }
        for (Integer id : epicsId) {
            removeHistory(id);
            epics.remove(id);
        }

    }

    // метод для проверки и изменения статуса Эпика при изменении статуса подзадач или их удалении
    private void updateEpicStatus(Epic epic) {
        List<SubTask> epicSubTasks = getEpicSubtasks(epic.getId());
        if (epicSubTasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean allDone = true;
        boolean allNew = true;

        for (SubTask subTask : epicSubTasks) {
            if (Status.IN_PROGRESS.equals(subTask.getStatus())) {

                epic.setStatus(Status.IN_PROGRESS);
                return;
            }
            if (!Status.DONE.equals(subTask.getStatus())) {
                allDone = false;
            }
            if (!Status.NEW.equals(subTask.getStatus())) {
                allNew = false;
            }
        }

        if (allNew) {
            epic.setStatus(Status.NEW);
        } else if (allDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void removeHistory(int id) {
        historyManager.remove(id);
    }

}



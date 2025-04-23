package com.yandex.app.manager;

import com.yandex.app.history.*;
import com.yandex.app.task.*;

import java.time.Duration;
import java.util.*;

public class InMemoryTasksManager implements TasksManager {

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    protected final Map<Integer, Task> tasks = new HashMap<>();
    protected final Map<Integer, Epic> epics = new HashMap<>();
    protected final Map<Integer, SubTask> subtasks = new HashMap<>();
    protected final TreeSet<Task> tasksByPriority = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    protected int id = 1;

    @Override
    public int addNewTask(Task task) {
        if (task.getStartTime() != null) {
            if (isOverlap(task)) {
                throw new IllegalArgumentException("Задача пересекается с другой задачей по времени выполнения");
            } else {
                tasksByPriority.add(task);
            }
        }
        task.setId(id);
        tasks.put(id++, task);
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        epic.setId(id);
        epics.put(id++, epic);
        return epic.getId();
    }

    @Override
    public int addNewSubtask(int epicId, SubTask subTask) {
        if (!epics.containsKey(epicId)) {
            System.out.println("Эписка с указанным ID нет.");
            return -1;
        }
        if (subTask.getStartTime() != null) {
            if (isOverlap(subTask)) {
                throw new IllegalArgumentException("Подзадача пересекается с другой задачей по времени выполнения");
            } else {
                tasksByPriority.add(subTask);
            }
        }
        Epic epic = epics.get(epicId);
        subTask.setId(id++);
        epic.addSubTask(subTask);
        subtasks.put(subTask.getId(), subTask);
        updateEpicStatus(epic);
        updateEpicDuration(epic);
        updateEpicStartAndEndTime(epic);
        return subTask.getId();
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public List<SubTask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return new ArrayList<>();
        } else {
            return epic.getSubtasks();
        }
    }

    public Epic getEpic(int id) {
        if (epics.containsKey(id)) {
            historyManager.add(epics.get(id));
            return epics.get(id);
        } else {
            System.out.println("Эпика с указанным ID нет в списке.");
            return null;
        }
    }

    @Override
    public Task getTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.add(tasks.get(id));
            return tasks.get(id);
        } else {
            System.out.println("Задачи с указанным ID нет в списке.");
            return null;
        }
    }

    @Override
    public SubTask getSubtask(int id) {
        if (subtasks.containsKey(id)) {
            historyManager.add(subtasks.get(id));
            return subtasks.get(id);
        } else {
            System.out.println("Подзадачи с указанным ID нет в списке.");
            return null;
        }
    }

    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            System.out.println("Задачи с таким ID нет в списке.");
            return;
        }
        if (tasks.get(task.getId()).getStartTime() != null) {
            tasksByPriority.remove(tasks.get(task.getId()));
        }

        if (task.getStartTime() != null) {
            if (isOverlap(task)) {
                throw new IllegalArgumentException("Задача пересекается с другой задачей по времени выполнения");
            }
            tasksByPriority.add(task);
        }
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic oldEpic = epics.get(epic.getId());
            oldEpic.setDescription(epic.getDescription());
            oldEpic.setName(epic.getName());
        } else {
            System.out.println("Эпика с таким ID нет в списке.");
        }
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        if (!subtasks.containsKey(subTask.getId())) {
            System.out.println("Подзадачи с таким ID нет в списке.");
            return;
        }
        if (subTask.getStartTime() != null) {
            if (isOverlap(subTask)) {
                throw new IllegalArgumentException("Задача пересекается с другой задачей по времени выполнения");
            } else {
                tasksByPriority.remove(subtasks.get(subTask.getId()));
                tasksByPriority.add(subTask);
            }
        }
        Epic epic = epics.get(subtasks
                .get(subTask.getId())
                .getEpicId());
        epic.removeSubTask(subTask.getId());
        epic.addSubTask(subTask);

        updateEpicStatus(epic);
        updateEpicDuration(epic);
        updateEpicStartAndEndTime(epic);
        subtasks.put(subTask.getId(), subTask);
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            historyManager.remove(id);
            if (tasks.get(id).getStartTime() != null) {
                tasksByPriority.remove(tasks.get(id));
            }
            tasks.remove(id);
        } else {
            System.out.println("Задача с указанным ID не найдена.");
        }
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            List<SubTask> subTasks = getEpicSubtasks(id);
            for (SubTask subTask : subTasks) {
                if (subTask.getStartTime() != null) {
                    tasksByPriority.remove(subTask);
                }
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
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            SubTask subTask = subtasks.get(id);
            Epic epic = epics.get(subTask.getEpicId());
            epic.removeSubTask(id);
            updateEpicStatus(epic);
            updateEpicDuration(epic);
            updateEpicStartAndEndTime(epic);
            historyManager.remove(id);
            if (subtasks.get(id).getStartTime() != null) {
                tasksByPriority.remove(subtasks.get(id));
            }
            subtasks.remove(id);
        } else {
            System.out.println("Подзадача с указанным ID не найден.");
        }
    }

    @Override
    public void deleteTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
            if (tasks.get(id).getStartTime() != null) {
                tasksByPriority.remove(tasks.get(id));
            }
        }
        tasks.clear();
    }

    @Override
    public void deleteSubtasks() {
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
            if (subtasks.get(id).getStartTime() != null) {
                tasksByPriority.remove(subtasks.get(id));
            }
        }
        for (Epic epic : epics.values()) {
            epic.setStatus(Status.NEW);
            epic.removeAllSubTask();
            epic.setDuration(Duration.ofMinutes(0));
            epic.setStartTime(null);
            epic.setEndTime(null);
        }
        subtasks.clear();
    }

    @Override
    public void deleteEpics() {
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
            if (subtasks.get(id).getStartTime() != null) {
                tasksByPriority.remove(subtasks.get(id));
            }
        }
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
        epics.clear();
    }

    protected void updateEpicStatus(Epic epic) {
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

    protected void updateEpicDuration(Epic epic) {
        Duration totalDurationOfEpicSubTasks = epic.getSubtasks().stream()
                .map(SubTask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
        epic.setDuration(totalDurationOfEpicSubTasks);
    }

    protected void updateEpicStartAndEndTime(Epic epic) {
        if (epic == null) {
            System.out.println("ЫЫЫ");
            return;
        }
            epic.getSubtasks().stream()
                    .filter(subTask -> subTask.getStartTime() != null)
                    .min(Comparator.comparing(SubTask::getStartTime))
                    .ifPresent(earliest -> epic.setStartTime(earliest.getStartTime()));

            epic.getSubtasks().stream()
                    .filter(subTask -> subTask.getStartTime() != null)
                    .max(Comparator.comparing(Task::getEndTime))
                    .ifPresent(latest -> epic.setEndTime(latest.getStartTime().plus(latest.getDuration())));
    }

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(tasksByPriority); // Возвращаем отсортированный список
    }

    public boolean isOverlap(Task task) {
        return tasksByPriority.stream()
                .anyMatch(existingTask -> isTasksOverlapping(existingTask, task));
    }

    private boolean isTasksOverlapping(Task task1, Task task2) {
        return task1.getStartTime().isBefore(task2.getEndTime()) &&
                task1.getEndTime().isAfter(task2.getStartTime());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}



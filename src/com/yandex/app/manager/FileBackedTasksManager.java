package com.yandex.app.manager;

import com.yandex.app.exceptions.ManagerSaveException;
import com.yandex.app.task.*;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;


public class FileBackedTasksManager extends InMemoryTasksManager {

    private static final String HEADER = "id,type,name,status,description,epic,startTime,duration\n";

    File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private String groupAllTasksToString() {
        StringBuilder allTasksToString = new StringBuilder();
        for (Task task : tasks.values()) {
            allTasksToString.append(task.toStringToFile());
        }

        for (Epic epic : epics.values()) {
            allTasksToString.append(epic.toStringToFile());
        }

        for (SubTask subTask : subtasks.values()) {
            allTasksToString.append(subTask.toStringToFile());
        }

        return allTasksToString.toString();

    }

    private void save() {
        try (Writer fileWriter = new FileWriter(file, false)) {
            fileWriter.write(HEADER);
            fileWriter.write(groupAllTasksToString());
        } catch (IOException exception) {
            throw new ManagerSaveException("Не удалось записать файл.");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (Reader fileReader = new FileReader(file)) {
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            bufferedReader.readLine();
            while (bufferedReader.ready()) {
                String line = bufferedReader.readLine();
                if (!line.equals("")) {
                    addTaskFromFile(line.split(","), fileBackedTasksManager);
                }
            }
            bufferedReader.close();
        } catch (IOException exception) {
            System.out.println("Не удалось прочитать файл.");
        }
        return fileBackedTasksManager;
    }

    private static void addTaskFromFile(String[] taskArr, FileBackedTasksManager fileBackedTasksManager) {
        switch (Type.valueOf(taskArr[1])) {
            case TASK: {
                Task task;
                if (taskArr[6].equals("null")) {
                    task = new Task(
                            Integer.parseInt(taskArr[0]),
                            taskArr[2],
                            taskArr[4],
                            Status.valueOf(taskArr[3])
                    );
                } else {
                    task = new Task(
                            Integer.parseInt(taskArr[0]),
                            taskArr[2],
                            taskArr[4],
                            Status.valueOf(taskArr[3]),
                            Duration.parse(taskArr[7]),
                            LocalDateTime.parse(taskArr[6])
                    );
                    fileBackedTasksManager.tasksByPriority.add(task);
                }
                fileBackedTasksManager.tasks.put(Integer.parseInt(taskArr[0]), task);

                if (Integer.parseInt(taskArr[0]) > fileBackedTasksManager.id) {
                    fileBackedTasksManager.id = Integer.parseInt(taskArr[0]);
                }
                break;
            }

            case EPIC: {
                if (taskArr[6].equals("null")) {
                    fileBackedTasksManager.epics.put(Integer.parseInt(taskArr[0]),
                            new Epic(Integer.parseInt(taskArr[0]),
                                    taskArr[2],
                                    taskArr[4],
                                    Status.valueOf(taskArr[3])
                            ));
                } else {
                    Epic epic = new Epic(Integer.parseInt(taskArr[0]),
                            taskArr[2],
                            taskArr[4],
                            Status.valueOf(taskArr[3]),
                            Duration.parse(taskArr[7]),
                            LocalDateTime.parse(taskArr[6])
                    );
                    fileBackedTasksManager.epics.put(Integer.parseInt(taskArr[0]), epic);
                    fileBackedTasksManager.updateEpicStartAndEndTime(epic);
                }
                if (Integer.parseInt(taskArr[0]) > fileBackedTasksManager.id) {
                    fileBackedTasksManager.id = Integer.parseInt(taskArr[0]);
                }
                break;
            }

            case SUBTASK: {
                SubTask subTask;
                if (taskArr[6].equals("null")) {
                    subTask = new SubTask(
                            Integer.parseInt(taskArr[0]),
                            Integer.parseInt(taskArr[5]),
                            taskArr[2],
                            taskArr[4],
                            Status.valueOf(taskArr[3])
                    );
                } else {
                    subTask = new SubTask(
                            Integer.parseInt(taskArr[0]),
                            Integer.parseInt(taskArr[5]),
                            taskArr[2],
                            taskArr[4],
                            Status.valueOf(taskArr[3]),
                            Duration.parse(taskArr[7]),
                            LocalDateTime.parse(taskArr[6])
                    );
                    fileBackedTasksManager.tasksByPriority.add(subTask);
                }
                fileBackedTasksManager.subtasks.put(Integer.parseInt(taskArr[0]), subTask);

                if (fileBackedTasksManager.epics.containsKey(Integer.parseInt(taskArr[5]))) {
                    fileBackedTasksManager.epics.get(Integer.parseInt(taskArr[5])).addSubTask(subTask);
                    fileBackedTasksManager.updateEpicStartAndEndTime(
                            fileBackedTasksManager.epics.get(Integer.parseInt(taskArr[5])));
                } else {
                    System.out.println("Подзадача с ID " + (Integer.parseInt(taskArr[0])) +
                            " не добавлена, так как не был найден Epic.");
                }

                if (Integer.parseInt(taskArr[0]) > fileBackedTasksManager.id) {
                    fileBackedTasksManager.id = Integer.parseInt(taskArr[0]);
                }
                break;
            }
        }
    }

    @Override
    public int addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int addNewSubtask(int epicId, SubTask subTask) {
        super.addNewSubtask(epicId, subTask);
        save();
        return subTask.getId();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(SubTask subTask) {
        super.updateSubtask(subTask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }
}


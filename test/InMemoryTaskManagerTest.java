import com.yandex.app.manager.InMemoryTasksManager;
import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTasksManagerTest {

    InMemoryTasksManager tasksManager = new InMemoryTasksManager();

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");

        final int taskId = tasksManager.addNewTask(task);
        final Task savedTask = tasksManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = tasksManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");

        final int taskId = tasksManager.addNewEpic(epic);
        final Epic savedTask = tasksManager.getEpic(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(epic, savedTask, "Задачи не совпадают.");

        final List<Epic> epics = tasksManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void addNewSubtask() {
        tasksManager.addNewEpic(new Epic("Epic for Test addNewSubTask", "Test Epic description"));
        SubTask subTask = new SubTask(1, "Test addNewSubTask", "Test addNewSubTask description");

        final int subTaskId = tasksManager.addNewSubtask(1, subTask);
        final SubTask savedTask = tasksManager.getSubtask(subTaskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(subTask, savedTask, "Задачи не совпадают.");

        final List<SubTask> subTasks = tasksManager.getSubtasks();

        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subTasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void getTasks() {
        Task task1 = new Task("Test#1 getTasks", "Test#1 getTasks description");
        Task task2 = new Task("Test#2 getTasks", "Test#2 getTasks description");
        Task task3 = new Task("Test#3 getTasks", "Test 3 getTasks description");

        tasksManager.addNewTask(task1);
        tasksManager.addNewTask(task2);
        tasksManager.addNewTask(task3);

        final List<Task> tasks = tasksManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(3, tasks.size(), "Неверное количество задач.");
        assertEquals(task1, tasks.get(0), "Задача #1 не совпадает.");
        assertEquals(task2, tasks.get(1), "Задача #2 не совпадает.");
        assertEquals(task3, tasks.get(2), "Задача #3 не совпадает.");
    }

    @Test
    void getEpics() {
        Epic epic1 = new Epic("Test#1 getTasks", "Test#1 getTasks description");
        Epic epic2 = new Epic("Test#2 getTasks", "Test#2 getTasks description");
        Epic epic3 = new Epic("Test#3 getTasks", "Test#3 getTasks description");

        tasksManager.addNewEpic(epic1);
        tasksManager.addNewEpic(epic2);
        tasksManager.addNewEpic(epic3);

        final List<Epic> epics = tasksManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(3, epics.size(), "Неверное количество задач.");
        assertEquals(epic1, epics.get(0), "Задача #1 не совпадает.");
        assertEquals(epic2, epics.get(1), "Задача #2 не совпадает.");
        assertEquals(epic3, epics.get(2), "Задача #3 не совпадает.");
    }

    @Test
    void getSubtasks() {
        tasksManager.addNewEpic(new Epic("Epic for Test getSubtasks", "getSubtasks description"));

        SubTask subTask1 = new SubTask(1, "Test#1 getSubtasks",
                "Test#1 getSubtasks description");
        SubTask subTask2 = new SubTask(1, "Test#2 getSubtasks",
                "Test#2 getSubtasks description");
        SubTask subTask3 = new SubTask(1, "Test#3 getSubtasks",
                "Test#3 getSubtasks description");

        tasksManager.addNewSubtask(1, subTask1);
        tasksManager.addNewSubtask(1, subTask2);
        tasksManager.addNewSubtask(1, subTask3);

        final List<SubTask> subTasks = tasksManager.getSubtasks();

        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(3, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask1, subTasks.get(0), "Задача #1 не совпадает.");
        assertEquals(subTask2, subTasks.get(1), "Задача #2 не совпадает.");
        assertEquals(subTask3, subTasks.get(2), "Задача #3 не совпадает.");
    }

    @Test
    void getEpicSubtasks() {
        tasksManager.addNewEpic(new Epic("Test getEpicSubtasks", "getEpicSubtasks description"));

        SubTask subTask1 = new SubTask(1, "Test#1 getEpicSubtasks",
                "Test#1 getSubtasks description");
        SubTask subTask2 = new SubTask(1, "Test#2 getEpicSubtasks",
                "Test#2 getSubtasks description");
        SubTask subTask3 = new SubTask(1, "Test#3 getEpicSubtasks",
                "Test#3 getSubtasks description");

        tasksManager.addNewSubtask(1, subTask1);
        tasksManager.addNewSubtask(1, subTask2);
        tasksManager.addNewSubtask(1, subTask3);

        final List<SubTask> epicSubtasks = tasksManager.getEpicSubtasks(1);

        assertNotNull(epicSubtasks, "Подзадачи не возвращаются.");
        assertEquals(subTask1, epicSubtasks.get(0), "Подзадача #1 не совпадает.");
        assertEquals(subTask2, epicSubtasks.get(1), "Подзадача #2 не совпадает.");
        assertEquals(subTask3, epicSubtasks.get(2), "Подзадача #3 не совпадает.");
        assertEquals(3, epicSubtasks.size(), "Неверное количество подзадач.");
    }

    @Test
    void getEpic() {
        Epic epic = new Epic("Test getEpic", "Test getEpic description");
        tasksManager.addNewEpic(epic);

        final Epic epicById = tasksManager.getEpic(1);

        assertNotNull(epicById, "Эпик не возвращается.");
        assertEquals(epic, epicById, "Эпик не совпадает.");
    }

    @Test
    void getTask() {
        Task task = new Task("Test getTask", "Test getTask description");
        tasksManager.addNewTask(task);

        final Task taskById = tasksManager.getTask(1);

        assertNotNull(taskById, "Задача не возвращаются.");
        assertEquals(task, taskById, "Задача не совпадает.");
    }

    @Test
    void getSubtask() {
        tasksManager.addNewEpic(new Epic("Epic for Test getSubtask", "getSubtask description"));
        SubTask subTask = new SubTask(1, "Test getSubtask", "Test getSubtask description");

        tasksManager.addNewSubtask(1, subTask);
        final SubTask subTaskById = tasksManager.getSubtask(2);

        assertNotNull(subTaskById, "Подзадача не возвращаются.");
        assertEquals(subTask, subTaskById, "Подзадача не совпадает.");
    }

    @Test
    void updateTask() {
        tasksManager.addNewTask(new Task("Test updateTask", "Test updateTask description"));
        Task updatedTask = new Task(1, "Test#2 updateTask", "Test#2 updateTask description");

        tasksManager.updateTask(updatedTask);

        final Task taskById = tasksManager.getTask(1);
        assertEquals(updatedTask, taskById, "Задача не совпадает.");
    }

    @Test
    void updateEpic() {
        tasksManager.addNewEpic(new Epic("Test updateEpic", "updateEpic description"));

        Epic updatedEpic = new Epic(1, "Test#2 updateEpic", "updateEpic#2 description");
        tasksManager.updateEpic(updatedEpic);

        final Epic epicById = tasksManager.getEpic(1);
        assertEquals(updatedEpic, epicById, "Эпик не совпадает.");
    }

    @Test
    void updateSubtask() {
        tasksManager.addNewEpic(new Epic("Epic for Test updateSubtask", "updateSubtask description"));
        tasksManager.addNewSubtask(1, (new SubTask(1, "Test updateSubtask",
                "Test updateSubtask description")));

        SubTask updatedSubTask = new SubTask(2, 1, "Test#2 updateSubtask",
                "Test#2 updateSubtask description", Status.IN_PROGRESS);
        tasksManager.updateSubtask(updatedSubTask);

        final SubTask subTaskById = tasksManager.getSubtask(2);
        assertEquals(updatedSubTask, subTaskById, "Подзадача не совпадает.");
    }

    @Test
    void deleteTask() {
        tasksManager.addNewTask(new Task("Test#1 deleteTask", "Test#1 deleteTask description"));
        Task task2 = new Task("Test#2 deleteTask", "Test#2 deleteTask description");
        tasksManager.addNewTask(task2);

        tasksManager.deleteTask(1);
        final List<Task> tasks = tasksManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task2, tasks.getFirst(), "Задачи не совпадают.");
    }

    @Test
    void deleteEpic() {
        tasksManager.addNewEpic(new Epic("Test#1 deleteEpic", "deleteEpic#1 description"));
        Epic epic2 = new Epic("Test#2 deleteEpic", "deleteEpic#2 description");
        tasksManager.addNewEpic(epic2);

        tasksManager.deleteEpic(1);
        final List<Epic> epics = tasksManager.getEpics();

        assertNotNull(epics, "Эпики не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество Эпиков.");
        assertEquals(epic2, epics.getFirst(), "Эпики не совпадают.");
    }

    @Test
    void deleteSubtask() {
        tasksManager.addNewEpic(new Epic("Epic for Test deleteSubtask", "deleteSubtask description"));
        tasksManager.addNewSubtask(1, (new SubTask(1, "Test#1 updateSubtask",
                "Test#1 updateSubtask description")));

        SubTask subTask2 = new SubTask(1, "Test#2 deleteSubtask",
                "Test#2 deleteSubtask description #2");
        tasksManager.addNewSubtask(1, subTask2);

        tasksManager.deleteSubtask(2);
        final List<SubTask> subTasks = tasksManager.getSubtasks();

        assertNotNull(subTasks, "Эпики не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество Эпиков.");
        assertEquals(subTask2, subTasks.getFirst(), "Эпики не совпадают.");
    }

    @Test
    void deleteTasks() {
        tasksManager.addNewTask(new Task("Test#1 deleteTasks", "Test#1 deleteTasks description"));
        tasksManager.addNewTask(new Task("Test#2 deleteTasks", "Test#2 deleteTasks description"));
        tasksManager.addNewTask(new Task("Test#3 deleteTasks", "Test 3 deleteTasks description"));

        tasksManager.deleteTasks();
        final List<Task> tasks = tasksManager.getTasks();

        assertEquals(0, tasks.size());
    }

    @Test
    void deleteSubtasks() {
        tasksManager.addNewEpic(new Epic("Epic#1 for Test deleteSubtasks",
                "deleteSubtasks description"));
        tasksManager.addNewSubtask(1, (new SubTask(1, "Test#1 deleteSubtasks",
                "Test#1 deleteSubtasks description")));
        tasksManager.addNewSubtask(1, (new SubTask(1, "Test#2 deleteSubtasks",
                "Test#2 deleteSubtasks description")));

        tasksManager.addNewEpic(new Epic("Epic#2 for Test deleteSubtasks",
                "deleteSubtasks description"));
        tasksManager.addNewSubtask(4, (new SubTask(4, "Test#3 deleteSubtasks",
                "Test#3 deleteSubtasks description")));

        tasksManager.deleteSubtasks();
        final List<SubTask> subTasks = tasksManager.getSubtasks();
        final List<Epic> epics = tasksManager.getEpics();

        assertEquals(0, subTasks.size());
        assertEquals(2, epics.size());
    }

    @Test
    void deleteEpics() {
        tasksManager.addNewEpic(new Epic("Test#1 deleteEpics", "deleteEpics description"));
        tasksManager.addNewEpic(new Epic("Test#2 deleteEpics", "deleteEpics description"));
        tasksManager.addNewEpic(new Epic("Test#3 deleteEpics", "deleteEpics description"));

        tasksManager.deleteEpics();
        final List<Epic> epics = tasksManager.getEpics();
        final List<SubTask> subTasks = tasksManager.getSubtasks();

        assertEquals(0, epics.size());
        assertEquals(0, subTasks.size());
    }

    @Test
    void updateEpicStatus() {
        tasksManager.addNewEpic(new Epic("Test updateEpicStatus","updateEpicStatus description"));
        tasksManager.addNewSubtask(1, (new SubTask(1, "Test#1 updateEpicStatus",
                "Test#1 updateEpicStatus description")));
        tasksManager.addNewSubtask(1, (new SubTask(1, "Test#2 updateEpicStatus",
                "Test#2 updateEpicStatus description")));

        assertEquals(Status.NEW, tasksManager.getEpic(1).getStatus());

        tasksManager.updateSubtask(new SubTask(2, 1, "Test#1 updateEpicStatus",
                "Test#1 updateEpicStatus description", Status.IN_PROGRESS));
        assertEquals(Status.IN_PROGRESS, tasksManager.getEpic(1).getStatus());


    }
}
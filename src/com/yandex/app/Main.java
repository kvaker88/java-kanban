package com.yandex.app;

import com.yandex.app.manager.TasksManager;
import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;

// данный класс используется исключительно для тестирования функционала
public class Main {
    public static void main(String[] args) {
        TasksManager tasksManager = new TasksManager();

        tasksManager.addNewTask(new Task ("Выкинуть мусор",
                "Нужно отнести все пластиковые бутылки и пакеты мусора на мусорку"));
        // создаём две задачи для тестирования по ТЗ
        tasksManager.addNewTask(new Task("Прибраться в комнате",
                "Нужно протереть пыль, пропылесосить, помыть пол, расставить все книги по полкам"));

        // создаём Эпик с двумя подзадачами
        tasksManager.addNewEpic(new Epic ("Купить квартиру",
                "Купить квартиру в Ленинградской области, у застройщика ЖК «Невский берег»"));
        tasksManager.addNewSubtask(3, new SubTask (3, "Накопить бюджет",
                "Необходимо собрать минимум 6.7 млн руб"));

        tasksManager.addNewSubtask(3, new SubTask (3, "Заключить договор с застройщиком",
                "Необходимо связаться с застройщиком, " +
                        "согласовать все детали покупки жилья, подписать документы, внести первоначальный взнос"));
        tasksManager.addNewSubtask(3, new SubTask (3, "Завершить сделку",
                "Внести остаток средств, по окончанию строительства дома, получения ключей от квартиры"));

        // создаём Эпик с одной подзадачей
        tasksManager.addNewEpic(new Epic ("Организовать семейный праздник",
                "Необходимо собраться всю семью в один день в кафе"));
        tasksManager.addNewSubtask(7, new SubTask (7, "Забронироваь кафе",
                "Нужно позвонить в кафе, забронировать большой стол"));

        System.out.println("Первый вывод:"); // выводим список добавленых выше задач
        System.out.println("-".repeat(120));
        System.out.println("Вывод всех обычных задач:");
        System.out.println(tasksManager.getTasks());
        System.out.println("-".repeat(120));
        System.out.println("Вывод всех Эпиков:");
        System.out.println(tasksManager.getEpics());
        System.out.println("-".repeat(120));
        System.out.println("Вывод всех подзадач:");
        System.out.println(tasksManager.getSubtasks());
        System.out.println("-".repeat(120));
        System.out.println("Вывод всех подзадач Эпика с ID 3:");
        System.out.println(tasksManager.getEpicSubtasks(3));
        System.out.println("-".repeat(120));

        tasksManager.deleteTask(1); // удаляем обычную задачу
        tasksManager.deleteEpic(7); // удаляем эпик
        tasksManager.deleteSubtask(6); // удаляем подзадачу

        System.out.println("-".repeat(120));

        tasksManager.updateTask(new Task(2, "Прибраться в доме", "Нужно протереть пыль, " +
                "пропылесосить, помыть пол, расставить все книги по полкам во всех комнатах дома"));
        tasksManager.updateEpic(new Epic(3, "Купить квартиру", "Купить квартиру " +
                "в Ленинградской области, у застройщика ЖК «Энфилд»")); // обновляем информацию эпика
        tasksManager.updateSubtask(new SubTask(4,"Накопить бюджет", "Необходимо собрать " +
                "минимум 7.1 млн руб", Status.IN_PROGRESS)); // обновляем описание и статус подзадачи
        System.out.println("-".repeat(120));


        System.out.println("Второй вывод:"); // снова выводим список задач для проверки изменений
        System.out.println("-".repeat(120));
        System.out.println(tasksManager.getTasks());
        System.out.println("-".repeat(120));
        System.out.println(tasksManager.getEpics());
        System.out.println("-".repeat(120));
        System.out.println(tasksManager.getSubtasks());
        System.out.println("-".repeat(120));


        tasksManager.deleteTasks(); // дополнительно, проверяем удаление всех задач в списке
        tasksManager.deleteSubtasks();
        tasksManager.deleteEpics();

        System.out.println("-".repeat(120));
        System.out.println(tasksManager.getTasks()); // а также, корректность вывода списка, при отсутствии задач
        System.out.println(tasksManager.getEpics());
        System.out.println(tasksManager.getSubtasks());
        System.out.println("-".repeat(120));
    }
}
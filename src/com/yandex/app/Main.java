package com.yandex.app;

import com.yandex.app.manager.TasksManager;
import com.yandex.app.task.Epic;
import com.yandex.app.task.Status;
import com.yandex.app.task.SubTask;
import com.yandex.app.task.Task;

// данный класс используется исключительно для тестирования функционала
public class Main {
    public static void main(String[] args) {
        TasksManager.addNewTask("Выкинуть мусор", "Нужно отнести все пластиковые " +
                "бутылки и пакеты мусора на мусорку");
        // создаём две задачи для тестирования по ТЗ
        TasksManager.addNewTask("Прибраться в комнате", "Нужно протереть пыль, " +
                "пропылесосить, помыть пол, расставить все книги по полкам");

        // создаём Эпик с двумя подзадачами
        TasksManager.addNewEpic("Купить квартиру", "Купить квартиру " +
                "в Ленинградской области, у застройщика ЖК «Невский берег»");
        TasksManager.addNewSubtask(3, "Накопить бюджет", "Необходимо собрать " +
                "минимум 6.7 млн руб");
        TasksManager.addNewSubtask(3, "Заключить договор с застройщиком",
                "Необходимо связаться с застройщиком, " +
                        "согласовать все детали покупки жилья, подписать документы, внести первоначальный взнос");
        TasksManager.addNewSubtask(3, "Завершить сделку", "Внести остаток средств, " +
                "по окончанию строительства дома, получения ключей от квартиры");

        // создаём Эпик с одной подзадачей
        TasksManager.addNewEpic("Организовать семейный праздник", "Необходимо " +
                "собраться всю семью в один день в кафе");
        TasksManager.addNewSubtask(7, "Забронироваь кафе", "Нужно позвонить в кафе, " +
                "забронировать большой стол");

        System.out.println("Первый вывод:"); // выводим список добавленых выше задач
        System.out.println("-".repeat(120));
        System.out.println("Вывод всех обычных задач:");
        System.out.println(TasksManager.getTasks());
        System.out.println("-".repeat(120));
        System.out.println("Вывод всех Эпиков:");
        System.out.println(TasksManager.getEpics());
        System.out.println("-".repeat(120));
        System.out.println("Вывод всех подзадач:");
        System.out.println(TasksManager.getSubtasks());
        System.out.println("-".repeat(120));
        System.out.println("Вывод всех подзадач Эпика с ID 3:");
        System.out.println(TasksManager.getEpicSubtasks(3));
        System.out.println("-".repeat(120));

        TasksManager.deleteTask(1); // удаляем обычную задачу
        TasksManager.deleteEpic(7); // удаляем эпик
        TasksManager.deleteSubtask(6); // удаляем подзадачу

        System.out.println("-".repeat(120));
        TasksManager.updateTask(new Task(2, "Прибраться в доме", "Нужно протереть пыль, " +
                "пропылесосить, помыть пол, расставить все книги по полкам во всех комнатах дома"));
        TasksManager.updateEpic(new Epic(3, "Купить квартиру", "Купить квартиру " +
                "в Ленинградской области, у застройщика ЖК «Энфилд»")); // обновляем информацию эпика
        TasksManager.updateSubtask(new SubTask(4, "Накопить бюджет", "Необходимо собрать " +
                "минимум 7.1 млн руб", Status.IN_PROGRESS)); // обновляем описание и статус подзадачи
        System.out.println("-".repeat(120));


        System.out.println("Второй вывод:"); // снова выводим список задач для проверки изменений
        System.out.println("-".repeat(120));
        System.out.println(TasksManager.getTasks());
        System.out.println("-".repeat(120));
        System.out.println(TasksManager.getEpics());
        System.out.println("-".repeat(120));
        System.out.println(TasksManager.getSubtasks());
        System.out.println("-".repeat(120));

        System.out.println("-".repeat(120));

        TasksManager.deleteTasks(); // дополнительно, проверяем удаление всех задач в списке
        TasksManager.deleteSubtasks();
        TasksManager.deleteEpics();

        System.out.println("-".repeat(120));
        System.out.println(TasksManager.getTasks()); // а также, корректность вывода списка, при отсутствии задач
        System.out.println(TasksManager.getEpics());
        System.out.println(TasksManager.getSubtasks());
        System.out.println("-".repeat(120));
    }
}


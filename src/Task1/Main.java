package Task1;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();

        // Вхідні параметри системи
        int storageSize = 10;       // Максимальна місткість сховища
        int totalItems = 8;       // Загальна кількість продукції для обробки
        int producersCount = 1;    // Кількість Виробників
        int consumersCount = 4;    // Кількість Споживачів

        System.out.println("Початок роботи. Сховище: " + storageSize +
                ", Продукції: " + totalItems + "\n");

        main.starter(storageSize, totalItems, producersCount, consumersCount);
    }

    private void starter(int storageSize, int totalItems, int producersCount, int consumersCount) {
        Manager manager = new Manager(storageSize);

        // Розподіляємо загальну кількість продукції між потоками
        int[] producerTasks = distributeItems(totalItems, producersCount);
        int[] consumerTasks = distributeItems(totalItems, consumersCount);

        List<Thread> threads = new ArrayList<>();

        // Створення та запуск потоків Виробників
        for (int i = 0; i < producersCount; i++) {
            System.out.println("Виробник " + (i + 1) + " має виробити: " + producerTasks[i]);
            Thread pThread = new Thread(new Producer(i + 1, producerTasks[i], manager));
            threads.add(pThread);
            pThread.start();
        }

        // Створення та запуск потоків Споживачів
        for (int i = 0; i < consumersCount; i++) {
            System.out.println("Споживач " + (i + 1) + " має спожити: " + consumerTasks[i]);
            Thread cThread = new Thread(new Consumer(i + 1, consumerTasks[i], manager));
            threads.add(cThread);
            cThread.start();
        }

        System.out.println("-----------------------------------");

        // Очікування завершення всіх потоків
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("-----------------------------------");
        System.out.println("Роботу завершено коректно. Всі елементи вироблено та спожито.");
        System.out.println("Залишок у сховищі: " + manager.storage.size());
    }

    // Метод для рівномірного розподілу продукції між потоками
    private int[] distributeItems(int totalItems, int entitiesCount) {
        int[] distribution = new int[entitiesCount];
        int baseTask = totalItems / entitiesCount; // Гарантована мінімальна кількість для кожного
        int remainder = totalItems % entitiesCount; // Залишок, який треба розподілити

        for (int i = 0; i < entitiesCount; i++) {
            distribution[i] = baseTask + (i < remainder ? 1 : 0);
        }
        return distribution;
    }
}

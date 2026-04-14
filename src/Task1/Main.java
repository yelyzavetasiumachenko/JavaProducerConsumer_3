package Task1;

import java.util.concurrent.CountDownLatch;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();

        int storageSize = 10;
        int totalItems = 8;
        int producersCount = 1;
        int consumersCount = 4;

        System.out.println("Початок роботи. Сховище: " + storageSize +
                ", Продукції: " + totalItems + "\n");

        main.starter(storageSize, totalItems, producersCount, consumersCount);
    }

    private void starter(int storageSize, int totalItems, int producersCount, int consumersCount) {
        Manager manager = new Manager(storageSize);

        int[] producerTasks = distributeItems(totalItems, producersCount);
        int[] consumerTasks = distributeItems(totalItems, consumersCount);

        // 1. СТВОРЮЄМО ЛІЧИЛЬНИК на кількість усіх потоків
        int totalThreads = producersCount + consumersCount;
        CountDownLatch latch = new CountDownLatch(totalThreads);

        System.out.println("-----------------------------------");

        // 2. Створюємо і одразу запускаємо потоки, передаючи їм latch
        for (int i = 0; i < producersCount; i++) {
            System.out.println("Виробник " + (i + 1) + " має виробити: " + producerTasks[i]);
            new Thread(new Producer(i + 1, producerTasks[i], manager, latch)).start();
        }

        for (int i = 0; i < consumersCount; i++) {
            System.out.println("Споживач " + (i + 1) + " має спожити: " + consumerTasks[i]);
            new Thread(new Consumer(i + 1, consumerTasks[i], manager, latch)).start();
        }

        // 3. Чекаємо, поки лічильник не стане 0
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("-----------------------------------");
        System.out.println("Роботу завершено коректно. Всі елементи вироблено та спожито.");
        System.out.println("Залишок у сховищі: " + manager.storage.size());
    }

    private int[] distributeItems(int totalItems, int entitiesCount) {
        int[] distribution = new int[entitiesCount];
        int baseTask = totalItems / entitiesCount;
        int remainder = totalItems % entitiesCount;

        for (int i = 0; i < entitiesCount; i++) {
            distribution[i] = baseTask + (i < remainder ? 1 : 0);
        }
        return distribution;
    }
}




























//package Task1;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class Main {
//
//    public static void main(String[] args) {
//        Main main = new Main();
//
//        // Вхідні параметри системи
//        int storageSize = 10;       // Максимальна місткість сховища
//        int totalItems = 8;       // Загальна кількість продукції для обробки
//        int producersCount = 1;    // Кількість Виробників
//        int consumersCount = 4;    // Кількість Споживачів
//
//        System.out.println("Початок роботи. Сховище: " + storageSize +
//                ", Продукції: " + totalItems + "\n");
//
//        main.starter(storageSize, totalItems, producersCount, consumersCount);
//    }
//
//    private void starter(int storageSize, int totalItems, int producersCount, int consumersCount) {
//        Manager manager = new Manager(storageSize);
//
//        int[] producerTasks = distributeItems(totalItems, producersCount);
//        int[] consumerTasks = distributeItems(totalItems, consumersCount);
//
//        // Створюємо списки для зберігання потоків перед запуском
//        List<Thread> producerThreads = new ArrayList<>();
//        List<Thread> consumerThreads = new ArrayList<>();
//
//        // 1. ЕТАП ПІДГОТОВКИ (Тільки створення)
//        for (int i = 0; i < producersCount; i++) {
//            System.out.println("Виробник " + (i + 1) + " має виробити: " + producerTasks[i]);
//            Thread pThread = new Thread(new Producer(i + 1, producerTasks[i], manager));
//            producerThreads.add(pThread); // Додаємо у список, НЕ запускаємо
//        }
//
//        for (int i = 0; i < consumersCount; i++) {
//            System.out.println("Споживач " + (i + 1) + " має спожити: " + consumerTasks[i]);
//            Thread cThread = new Thread(new Consumer(i + 1, consumerTasks[i], manager));
//            consumerThreads.add(cThread); // Додаємо у список, НЕ запускаємо
//        }
//
//        // Всі налаштування завершено, друкуємо лінію
//        System.out.println("-----------------------------------");
//
//        // 2. ЕТАП ЗАПУСКУ
//        for (Thread t : producerThreads) t.start();
//        for (Thread t : consumerThreads) t.start();
//
//        // 3. ОЧІКУВАННЯ ЗАВЕРШЕННЯ
//        try {
//            for (Thread t : producerThreads) t.join();
//            for (Thread t : consumerThreads) t.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("-----------------------------------");
//        System.out.println("Роботу завершено коректно. Всі елементи вироблено та спожито.");
//        System.out.println("Залишок у сховищі: " + manager.storage.size());
//    }
//
////    private void starter(int storageSize, int totalItems, int producersCount, int consumersCount) {
////        Manager manager = new Manager(storageSize);
////
////        // Розподіляємо загальну кількість продукції між потоками
////        int[] producerTasks = distributeItems(totalItems, producersCount);
////        int[] consumerTasks = distributeItems(totalItems, consumersCount);
////
////        List<Thread> threads = new ArrayList<>();
////
////        // Створення та запуск потоків Виробників
////        for (int i = 0; i < producersCount; i++) {
////            System.out.println("Виробник " + (i + 1) + " має виробити: " + producerTasks[i]);
////            Thread pThread = new Thread(new Producer(i + 1, producerTasks[i], manager));
////            threads.add(pThread);
////            pThread.start();
////        }
////
////        // Створення та запуск потоків Споживачів
////        for (int i = 0; i < consumersCount; i++) {
////            System.out.println("Споживач " + (i + 1) + " має спожити: " + consumerTasks[i]);
////            Thread cThread = new Thread(new Consumer(i + 1, consumerTasks[i], manager));
////            threads.add(cThread);
////            cThread.start();
////        }
////
////        System.out.println("-----------------------------------");
////
////        // Очікування завершення всіх потоків
////        for (Thread thread : threads) {
////            try {
////                thread.join();
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        }
////
////        System.out.println("-----------------------------------");
////        System.out.println("Роботу завершено коректно. Всі елементи вироблено та спожито.");
////        System.out.println("Залишок у сховищі: " + manager.storage.size());
////    }
//
//    // Метод для рівномірного розподілу продукції між потоками
//    private int[] distributeItems(int totalItems, int entitiesCount) {
//        int[] distribution = new int[entitiesCount];
//        int baseTask = totalItems / entitiesCount; // Гарантована мінімальна кількість для кожного
//        int remainder = totalItems % entitiesCount; // Залишок, який треба розподілити
//
//        for (int i = 0; i < entitiesCount; i++) {
//            distribution[i] = baseTask + (i < remainder ? 1 : 0);
//        }
//        return distribution;
//    }
//}

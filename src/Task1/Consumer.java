package Task1;

import java.util.concurrent.CountDownLatch;

public class Consumer implements Runnable {
    private final int id;
    private final int itemNumbers;
    private final Manager manager;
    private final CountDownLatch latch;

    public Consumer(int id, int itemNumbers, Manager manager, CountDownLatch latch) {
        this.id = id;
        this.itemNumbers = itemNumbers;
        this.manager = manager;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < itemNumbers; i++) {
                String item;
                try {
                    // Якщо товарів немає (0), повідомляємо, що доведеться чекати
                    if (manager.empty.availablePermits() == 0) {
                        System.out.println("⌛ Сховище ПОРОЖНЄ! Споживач " + id + " чекає на новий товар...");
                    }

                    manager.empty.acquire(); // Чекаємо, поки з'явиться хоча б 1 товар
                    manager.access.acquire(); // Блокуємо сховище

                    item = manager.storage.get(0);
                    manager.storage.remove(0);

                    System.out.println("🔴 Споживач " + id + " ВЗЯВ: " + item +
                            " | У сховищі: " + manager.storage.size());

                    manager.access.release(); // Звільняємо сховище
                    manager.full.release();   // Повідомляємо, що з'явилося вільне місце

                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        } finally {
            latch.countDown();
        }
    }
}
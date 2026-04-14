package Task1;

import java.util.concurrent.CountDownLatch;

public class Producer implements Runnable {
    private final int id;
    private final int itemNumbers;
    private final Manager manager;
    private final CountDownLatch latch;

    public Producer(int id, int itemNumbers, Manager manager, CountDownLatch latch) {
        this.id = id;
        this.itemNumbers = itemNumbers;
        this.manager = manager;
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < itemNumbers; i++) {

                try {
                    if (manager.full.availablePermits() == 0) {
                        System.out.println("⏳ Сховище ПОВНЕ! Виробник " + id + " очікує звільнення місця...");
                    }

                    manager.full.acquire();
                    manager.access.acquire();

                    String item = "Item-" + (i + 1) + " (від В" + id + ")";
                    manager.storage.add(item);

                    System.out.println("🟢 Виробник " + id + " ДОДАВ: " + item +
                            " | У сховищі: " + manager.storage.size());

                    manager.access.release();
                    manager.empty.release();

                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        } finally {
            latch.countDown();
        }
    }
}
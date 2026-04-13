package Task1;

public class Producer implements Runnable {
    private final int id;
    private final int itemNumbers;
    private final Manager manager;

    public Producer(int id, int itemNumbers, Manager manager) {
        this.id = id;
        this.itemNumbers = itemNumbers;
        this.manager = manager;
    }

    @Override
    public void run() {
        for (int i = 0; i < itemNumbers; i++) {
            try {
                // Якщо вільних місць немає (0), повідомляємо, що доведеться чекати
                if (manager.full.availablePermits() == 0) {
                    System.out.println("⏳ Сховище ПОВНЕ! Виробник " + id + " очікує звільнення місця...");
                }

                manager.full.acquire(); // Чекаємо на вільне місце
                manager.access.acquire(); // Блокуємо сховище для інших

                String item = "Item-" + (i + 1) + " (від В" + id + ")";
                manager.storage.add(item);

                System.out.println("🟢 Виробник " + id + " ДОДАВ: " + item +
                        " | У сховищі: " + manager.storage.size());

                manager.access.release(); // Звільняємо сховище
                manager.empty.release();  // Повідомляємо, що з'явився новий товар

                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}



















//package Task1;
//
//public class Producer implements Runnable {
//    private final int id;
//    private final int itemNumbers;
//    private final Manager manager;
//
//    public Producer(int id, int itemNumbers, Manager manager) {
//        this.id = id;
//        this.itemNumbers = itemNumbers;
//        this.manager = manager;
//        // Запуск потоку прибрано з конструктора для кращого контролю
//    }
//
//    @Override
//    public void run() {
//        for (int i = 0; i < itemNumbers; i++) {
//            try {
//                manager.full.acquire(); // Чекаємо на вільне місце
//                manager.access.acquire(); // Блокуємо сховище для інших
//
//                String item = "Item-" + i + " (від Виробника " + id + ")";
//                manager.storage.add(item);
//                System.out.println("Виробник " + id + " ДОДАВ: " + item);
//
//                manager.access.release(); // Звільняємо сховище
//                manager.empty.release();  // Сигналізуємо, що з'явився новий товар
//
//                // Невелика затримка для більш наочного виводу в консоль
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}

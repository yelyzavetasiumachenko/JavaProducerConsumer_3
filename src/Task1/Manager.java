package Task1;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Manager {

    public Semaphore access;
    public Semaphore full; // Кількість вільних місць
    public Semaphore empty; // Кількість наявної продукції

    public ArrayList<String> storage = new ArrayList<>();

    public Manager(int storageSize) {
        access = new Semaphore(1);
        full = new Semaphore(storageSize);
        empty = new Semaphore(0);
    }
}
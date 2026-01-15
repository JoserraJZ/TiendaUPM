package upm;

import java.util.*;

public class Catalog<T> {
    final TreeMap<Integer, T> items = new TreeMap<>();

    private final int maxElements;

    public Catalog(int maxElements) { this.maxElements = maxElements; }
    public Catalog() { this.maxElements = Integer.MAX_VALUE; }

    public void add(int id, T item) {
        if (id == -1) {
            id = items.isEmpty() ? 0 : items.lastKey() + 1;
        }
        if (items.size()<maxElements){
            items.put(id, item);
        }
    }

    public T remove(int id) {
        T item = items.get(id);
        items.remove(id);
        return item;
    }

    public void list() {
        System.out.println("Catalog:");

        for (Map.Entry<Integer, T> entry : items.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    public T getById(int id) {
        return items.get(id);
    }
    public boolean doesIdExist(int id){
        return items.containsKey(id);
    }
}

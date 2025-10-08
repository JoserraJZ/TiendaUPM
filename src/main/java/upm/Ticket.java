package main.java.upm;

import java.util.*;

public class Ticket {

    private final Map<Integer, TicketItem> items ;
    private int cantidadProductos;

    //Inicializa Ticket
    public Ticket() {
        this.items = new LinkedHashMap<>();
        this.cantidadProductos = 0;
    }

    // Agrega un producto y su cantidad al ticket
    public void add(Product product, int amount) {
        int id = product.getIdProducto();
        if (items.containsKey(id)) {
            TicketItem item = items.get(id);
            // Crear un nuevo TicketItem con la cantidad sumada
            items.put(id, new TicketItem(product, item.getQuantity() + amount));
        } else {
            items.put(id, new TicketItem(product, amount));
        }
        this.cantidadProductos += amount;
    }

    // Ejemplo de método buscar
    public int buscar(int idProducto) {
        return items.containsKey(idProducto) ? items.get(idProducto).getQuantity() : 0;
    }

    // Getter para los items
    public Collection<TicketItem> getItems() {
        return items.values();
    }

    public int getCantidadProductos() {
        return cantidadProductos;
    }
}

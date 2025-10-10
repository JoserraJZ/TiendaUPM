package upm;

import java.util.*;

public class Ticket {

    private final Map<Integer, TicketItem> items ;
    //private int cantidadProductos;

    //Inicializa Ticket
    public Ticket() {
        this.items = new LinkedHashMap<>();
        // this.cantidadProductos = 0;
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
        // this.cantidadProductos += amount;
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
        return items.size();
    }


    public boolean ticketRemove(int idProducto) {
        if (items.containsKey(idProducto)) {
            // Obtener el item que vamos a eliminar
            TicketItem item = items.get(idProducto);
            // Restar su cantidad al total de productos
            // cantidadProductos -= item.getQuantity();
            // Eliminar del mapa
            items.remove(idProducto);
            System.out.println("Producto con ID " + idProducto + " eliminado correctamente.");
            return true;
        } else {
            System.out.println("El producto con ID " + idProducto + " no existe en el ticket.");
            return false;
        }
    }

    public void ticketPrint() {
        double totalPrice = 0.0;
        double totalDiscount = 0.0;

        if (items.isEmpty()) {
            System.out.println("No hay productos en el ticket.");
            return;
        }

        for (TicketItem item : items.values()) {
            Product p = item.getProduct();
            double discount = this.calcularDescuento(); // Asegúrate de que Product tenga getDescuento()
            int quantity = item.getQuantity();
            double price = p.getPrecio();
            for (int i = 0; i < quantity; i++) {
                System.out.printf("{class:Product, id:%d, name:'%s', category:%s, price:%.1f} **discount -%.1f\n",
                        p.getIdProducto(), p.getNombreProducto(), p.getCat(), price, discount);
            }
            totalPrice += price * quantity;
            totalDiscount += discount * quantity;
        }

        double finalPrice = totalPrice - totalDiscount;
        System.out.printf("Total price: %.1f\n", totalPrice);
        System.out.printf("Total discount: %.1f\n", totalDiscount);
        System.out.printf("Final Price: %.1f\n", finalPrice);
        System.out.println("ticket print: ok");
    }

    public double calcularDescuento() {
        Map<Category, Integer> categoriaCantidad = new HashMap<>();
        double totalDescuento = 0.0;

        // Contar productos por categoría
        for (TicketItem item : items.values()) {
            Category cat = item.getProduct().getCat();
            categoriaCantidad.put(cat, categoriaCantidad.getOrDefault(cat, 0) + item.getQuantity());
        }

        // Sumar descuentos de categorías con más de un producto
        for (Map.Entry<Category, Integer> entry : categoriaCantidad.entrySet()) {
            if (entry.getValue() > 1) {
                totalDescuento += entry.getKey().getDiscountPercent();
            }
        }

        return totalDescuento;
    }




}

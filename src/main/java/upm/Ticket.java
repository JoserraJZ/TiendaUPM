package upm;

import java.time.LocalDateTime;
import java.util.*;

public class Ticket {

    private final Map<Integer, TicketItem> items ;
    private int ticketId;
    private String lastUpdateDate;
    //private int cantidadProductos;

    //Inicializa Ticket
    public Ticket(int id) {
        this.items = new LinkedHashMap<>();
        // this.cantidadProductos = 0;
        ticketId=id;
        lastUpdateDate= LocalDateTime.now()+"-"+ticketId;
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

        // Ordenar los productos por ID descendente
        List<TicketItem> ordenados = new ArrayList<>(items.values());
        ordenados.sort((a, b) -> Integer.compare(b.getProduct().getIdProducto(), a.getProduct().getIdProducto()));

        for (TicketItem item : ordenados) {
            Product p = item.getProduct();
            int quantity = item.getQuantity();
            double price = p.getPrecio();
            double discount = (double) p.getCat().getDiscountPercent() /100; // 10% si hay 2 o más

            if (quantity < 2) {
                System.out.printf(Locale.US,
                        "{class:Product, id:%d, name:'%s', category:%s, price:%.1f}%n",
                        p.getIdProducto(), p.getNombreProducto(), p.getCat(), price);
            } else {
                for (int i = 0; i < quantity; i++) {
                    double descuentoUnitario = price * discount;
                    System.out.printf(Locale.US,
                            "{class:Product, id:%d, name:'%s', category:%s, price:%.1f} **discount -%.1f%n",
                            p.getIdProducto(), p.getNombreProducto(), p.getCat(), price, descuentoUnitario);
                    totalDiscount += descuentoUnitario;
                }
            }

            totalPrice += price * quantity;
        }

        double finalPrice = totalPrice - totalDiscount;
        System.out.printf(Locale.US, "Total price: %.1f%n", totalPrice);
        System.out.printf(Locale.US, "Total discount: %.1f%n", totalDiscount);
        System.out.printf(Locale.US, "Final Price: %.1f%n", finalPrice);
    }

}

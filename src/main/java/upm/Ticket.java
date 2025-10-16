package upm;

import java.util.*;

public class Ticket {

    private final Map<Integer, TicketItem> items ;

    public Ticket() {
        this.items = new LinkedHashMap<>();
    }

    public void add(Product product, int amount) {
        int id = product.getIdProducto();
        if (items.containsKey(id)) {
            TicketItem item = items.get(id);
            items.put(id, new TicketItem(product, item.getQuantity() + amount));
        } else {
            items.put(id, new TicketItem(product, amount));
        }
    }

    public Collection<TicketItem> getItems() {
        return items.values();
    }

    public boolean ticketRemove(int idProducto) {
        if (items.containsKey(idProducto)) {
            TicketItem item = items.get(idProducto);
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
        double totalDiscount = calcularDescuento();

        if (items.isEmpty()) {
            System.out.println("No hay productos en el ticket.");
            return;
        }

        List<TicketItem> ordenados = new ArrayList<>(items.values());
        ordenados.sort((a, b) -> Integer.compare(b.getProduct().getIdProducto(), a.getProduct().getIdProducto()));

        for (TicketItem item : ordenados) {
            Product p = item.getProduct();
            int quantity = item.getQuantity();
            double price = p.getPrecio();
                double descuentoUnitario=0;
                if(quantity>=2) {
                        descuentoUnitario=price * p.getCat().getDiscountPercent() / 100.0;
                }
                for (int i = 0; i < quantity; i++) {
                    if (descuentoUnitario!=0) {
                        System.out.printf(Locale.US,
                                "{class:Product, id:%d, name:'%s', category:%s, price:%.1f} **discount -%.1f%n",
                                p.getIdProducto(), p.getNombreProducto(), p.getCat(), price, descuentoUnitario);
                    }else {
                        System.out.printf(Locale.US,
                                "{class:Product, id:%d, name:'%s', category:%s, price:%.1f}\n",p.getIdProducto(), p.getNombreProducto(), p.getCat(), price);
                    }
                }

            totalPrice += price * quantity;
        }

        double finalPrice = totalPrice - totalDiscount;
        System.out.printf(Locale.US, "Total price: %.1f%n", totalPrice);
        System.out.printf(Locale.US, "Total discount: %.1f%n", totalDiscount);
        System.out.printf(Locale.US, "Final Price: %.1f%n", finalPrice);
    }

    public double calcularDescuento() {
        double totalDescuento = 0.0;
        for (TicketItem item : items.values()) {
            int quantity = item.getQuantity();
            if (quantity >= 2) {
                Product p = item.getProduct();
                double descuentoUnitario = p.getPrecio() * p.getCat().getDiscountPercent() / 100.0;
                totalDescuento += descuentoUnitario * quantity;
            }
        }
        return totalDescuento;
    }
}

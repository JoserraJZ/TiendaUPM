package upm;

import java.time.LocalDateTime;
import java.util.*;

public class Ticket {

    private final Map<Integer, TicketItem> items ;
    private final int id;
    private String timestampID;

    public Ticket(int id) {
        this.items = new LinkedHashMap<>();

        this.id =id;
        this.timestampID = LocalDateTime.now()+"-"+ this.id;
    }


    //TODO: HACER ESTO CON UN ITEMS.MERGE
    public void addProducts(Product product, int amount) {
        int id = product.getId();

        if (items.containsKey(id)) {
            TicketItem item = items.get(id);

            items.put(id, new TicketItem(product, item.getQuantity() + amount));
        } else {
            items.put(id, new TicketItem(product, amount));
        }
    }


    public boolean removeProduct(int productId) {
        if (items.remove(productId) != null) {
            System.out.println("Producto con ID " + productId + " eliminado correctamente.");
            return true;
        }
        System.out.println("El producto con ID " + productId + " no existe en el ticket.");
        return false;
    }

    public void printTicket() {
        if (items.isEmpty()) {
            System.out.println("No hay productos en el ticket.");
            return;
        }

        // Ordenar los productos por ID descendente
        List<TicketItem> sorted = new ArrayList<>(items.values());
        sorted.sort((a, b) -> Integer.compare(b.getProduct().getId(), a.getProduct().getId()));

        double totalPrice = 0.0;
        double totalDiscount = 0.0;


        for (TicketItem ticketItem : sorted) {
            Product prod = ticketItem.getProduct();
            int quantity = ticketItem.getQuantity();
            double price = prod.getPrice();
            double discount = (double) prod.getCategory().getDiscountPercent() /100;

            if (quantity < 2) {
                System.out.println(prod);
            } else {
                for (int i = 0; i < quantity; i++) {
                    double unitDiscount = price * discount;
                    System.out.printf(Locale.US, "%s **discount -%.1f%n", prod, unitDiscount);
                    totalDiscount += unitDiscount;
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

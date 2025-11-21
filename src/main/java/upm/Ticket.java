package upm;

import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class Ticket {

    private final Map<Integer, TicketItem> items ;
    private final String id;
    private String timestampID;
    private TicketState currentState;

    public Ticket(String id) {
        this.id = (id == null) ? RandomGenerator.generateTicketId() : id;
        this.items = new LinkedHashMap<>();
        this.currentState = TicketState.VACIO;
    }


    //TODO: HACER ESTO CON UN ITEMS.MERGE
    public void addProducts(Product product, int amount) {
        if (currentState != TicketState.CERRADO){
            int id = product.getId();

            if (items.containsKey(id)) {
                TicketItem item = items.get(id);

                items.put(id, new TicketItem(product, item.getQuantity() + amount));
            } else {
                items.put(id, new TicketItem(product, amount));
            }
        }
    }


    public boolean removeProduct(int productId) {
        if (currentState != TicketState.CERRADO){
            if (items.remove(productId) != null) {
                System.out.println("Producto con ID " + productId + " eliminado correctamente.");
                return true;
            }
            System.out.println("El producto con ID " + productId + " no existe en el ticket.");
            return false;
        }
        return false;
    }

    public void printTicket() {
        this.currentState = TicketState.CERRADO;

        System.out.println(this);
    }

    String getId() {
        return id;
    }

    TicketState getCurrentState() {
        return currentState;
    }



    @Override
    public String toString() {
        List<TicketItem> sorted = new ArrayList<>(items.values());
        sorted.sort((a, b) -> Integer.compare(b.getProduct().getId(), a.getProduct().getId()));

        double totalPrice = 0.0;
        double totalDiscount = 0.0;

        StringBuilder sb = new StringBuilder("Ticket : ");
        sb.append(timestampID).append("\n");


        for (TicketItem ticketItem : sorted) {
            Product prod = ticketItem.getProduct();
            int quantity = ticketItem.getQuantity();
            double price = prod.getPrice();
            double discount = (double) prod.getCategory().getDiscountPercent() / 100;

            if (quantity < 2) {
                sb.append(prod).append("\n");
            } else {
                for (int i = 0; i < quantity; i++) {
                    double unitDiscount = price * discount;
                    sb.append(String.format(Locale.US, "%s **discount -%.1f%n", prod, unitDiscount));
                    totalDiscount += unitDiscount;
                }
            }

            totalPrice += price * quantity;
        }

        double finalPrice = totalPrice - totalDiscount;
        sb.append(String.format(Locale.US, "Total price: %.1f%n", totalPrice));
        sb.append(String.format(Locale.US, "Total discount: %.1f%n", totalDiscount));
        sb.append(String.format(Locale.US, "Final Price: %.1f%n", finalPrice));

        return sb.toString();
    }
}

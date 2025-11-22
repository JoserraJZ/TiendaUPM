// java
package upm;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class Ticket {

    private final Map<Integer, TicketItem> items ;
    private String id;
    private String timestampID;
    private TicketState currentState;

    private String chasId;
    private String userId;

    public Ticket(String id) {
        this.id = (id == null) ? RandomGenerator.generateTicketId() : id;
        this.items = new LinkedHashMap<>();
        this.currentState = TicketState.EMPTY;
    }

    public Ticket(String id, String chasId, String userId) {
        this.id = (id == null) ? RandomGenerator.generateTicketId() : id;
        this.items = new LinkedHashMap<>();
        //this.timestampID = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));
        this.currentState = TicketState.EMPTY;
        this.chasId = chasId;
        this.userId = userId;
    }


    //TODO: HACER ESTO CON UN ITEMS.MERGE
    public void addProducts(Product product, int amount) {
        if (currentState != TicketState.CLOSE){
            int id = product.getId();

            if (items.containsKey(id)) {
                TicketItem item = items.get(id);

                if (product instanceof ProductMeeting || product instanceof ProductCampusFood){
                    items.put(id, new TicketItem(product, 1));
                }else {
                    items.put(id, new TicketItem(product, item.getQuantity() + amount));
                }

            } else {
                items.put(id, new TicketItem(product, amount));
            }
            this.currentState = TicketState.OPEN;
        }
    }


    public boolean removeProduct(int productId) {
        if (currentState != TicketState.CLOSE){
            if (items.remove(productId) != null) {
                //System.out.println("Producto con ID " + productId + " eliminado correctamente.");
                return true;
            }
            System.out.println("El producto con ID " + productId + " no existe en el ticket.");
            return false;
        }
        return false;
    }

    public void printTicket() {
        this.currentState = TicketState.CLOSE;
        id = id + "-"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));
        System.out.println(this);
    }

    public void printTicketNoClose() {
        System.out.println(this);
    }

    String getId() {
        return id;
    }

    TicketState getCurrentState() {
        return currentState;
    }

    public String getChasId() {
        return chasId;
    }

    public void setChasId(String chasId) {
        this.chasId = chasId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }



    @Override
    public String toString() {
        List<TicketItem> sorted = new ArrayList<>(items.values());
        sorted.sort((a, b) -> Integer.compare(b.getProduct().getId(), a.getProduct().getId()));

        double totalPrice = 0.0;
        double totalDiscount = 0.0;

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.0#######", symbols); // hasta 2 decimales, sin ceros innecesarios
        String formatted = null;
        StringBuilder sb = new StringBuilder("Ticket : ");
        sb.append(id).append("\n");;

        /*
        if (this.currentState == TicketState.CLOSE) {
            sb.append("-"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"))).append("\n");;
        } else {
            sb.append("\n");
        }

         */

        for (TicketItem ticketItem : sorted) {
            Product prod = ticketItem.getProduct();
            int quantity = ticketItem.getQuantity();
            double price = prod.getPrice();




            if (quantity < 2) {
                sb.append(prod).append("\n");
            } else {
                if (prod.getCategory()!=null) {
                    double discount = (double) prod.getCategory().getDiscountPercent() / 100;
                    for (int i = 0; i < quantity; i++) {
                        double unitDiscount = price * discount;
                        formatted=df.format(unitDiscount);
                        sb.append(String.format(Locale.US, "%s **discount -%s%n", prod, formatted));
                        totalDiscount += unitDiscount;
                    }
                }else {
                    if (prod instanceof CustomizableProduct) {
                        sb.append(String.format(Locale.US, "%s%n", prod).repeat(quantity));
                    }
                    else{
                        sb.append(String.format(Locale.US, "%s%n", prod));
                        if (prod instanceof ProductMeeting){ProductMeeting pM=(ProductMeeting) prod; quantity=((ProductMeeting) pM).getCurrentParticipants();}
                            else{
                            ProductCampusFood pM=(ProductCampusFood) prod; quantity=((ProductCampusFood) pM).getCurrentParticipants();
                            }
                    }
                }
            }

            totalPrice += price * quantity;
        }

        double finalPrice = totalPrice - totalDiscount;
        formatted=df.format(totalPrice);
        sb.append(String.format(Locale.US, "  Total price: %s%n", formatted));
        formatted=df.format(totalDiscount);
        sb.append(String.format(Locale.US, "  Total discount: %s%n", formatted));
        formatted=df.format(finalPrice);
        sb.append(String.format(Locale.US, "  Final Price: %s", formatted));

        return sb.toString();
    }
}

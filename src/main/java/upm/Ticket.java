// java
package main.java.upm;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

public class Ticket {

    private final Map<Integer, ArrayList<Product>> items ;
    private String id;
    private String timestampID;
    private TicketState currentState;

    private String cashId;
    private String userId;

    public Ticket(String id) {
        this.id = (id == null) ? RandomGenerator.generateTicketId() : id;
        this.items = new LinkedHashMap<>();
        this.currentState = TicketState.EMPTY;
    }

    public Ticket(String id, String cashId, String userId) {
        this.id = (id == null) ? RandomGenerator.generateTicketId() : id;
        this.items = new LinkedHashMap<>();
        //this.timestampID = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));
        this.currentState = TicketState.EMPTY;
        this.cashId = cashId;
        this.userId = userId;
    }


    //TODO: HACER ESTO CON UN ITEMS.MERGE
    public void addProducts(Product product, int amount) {
        if (currentState != TicketState.CLOSE){


            if (items.containsKey(product.getId())) {
                ArrayList<Product> existingList = items.get(product.getId());
                if (product instanceof ProductMeeting || product instanceof ProductCampusFood){
                    existingList.removeFirst();
                    existingList.add(product);
                    items.put(product.getId(), existingList);
                }else {
                    for (int i = 0; i < amount; i++) {
                        existingList.add(product);
                    }
                    items.put(product.getId(), existingList);
                }
            } else {
                ArrayList<Product> newList= new ArrayList<>();
                for (int i = 0; i < amount; i++) {
                    newList.add(product);
                }
                items.put(product.getId(), newList);
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

    public String getCashId() {
        return cashId;
    }

    public void setCashId(String cashId) {
        this.cashId = cashId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Product> ordenarPorPrecio(ArrayList<Product> arrayArticulosmismoId){
        ArrayList<Product> sorted = new ArrayList<>(arrayArticulosmismoId);
        Collections.sort(sorted, Comparator.comparingDouble(Product::getPrice));
        return sorted;
    }

    @Override
    public String toString() {


        //sorted.sort((a, b) -> Integer.compare(b.getProduct().getId(), a.getProduct().getId()));

        double totalPrice = 0.0;
        double totalDiscount = 0.0;
        List<ArrayList<Product>> sorted = new ArrayList<>(items.values());

        String formatted = null;
        StringBuilder sb = new StringBuilder("Ticket : ");
        sb.append(id).append("\n");
        ;

        /*
        if (this.currentState == TicketState.CLOSE) {
            sb.append("-"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"))).append("\n");;
        } else {
            sb.append("\n");
        }

         */
        for (ArrayList<Product> prodAgrupados: sorted) {

            //ArrayList<Product> productosAgrupados = items.get(id);
            int quantity = prodAgrupados.size();
            for (Product prod : prodAgrupados) {

                double price = prod.getPrice();


                if (quantity < 2) {
                    sb.append(prod).append("\n");
                } else {
                    if (prod.getCategory() != null) {
                        double discount = (double) prod.getCategory().getDiscountPercent() / 100;
                        double unitDiscount = price * discount;
                        formatted = formatDouble(unitDiscount);
                        sb.append(String.format(Locale.US, "%s **discount -%s%n", prod, formatted));
                        totalDiscount += unitDiscount;
                    } else {
                        if (prod instanceof CustomizableProduct) {
                            sb.append(String.format(Locale.US, "%s%n", prod).repeat(quantity));
                        } else {
                            sb.append(String.format(Locale.US, "%s%n", prod));

                        }
                    }
                }
                if (prod instanceof ProductMeeting) {
                    totalPrice += ((ProductMeeting) prod).calculateCurrentPrice();
                } else if (prod instanceof ProductCampusFood) {
                    totalPrice += ((ProductCampusFood) prod).calculateCurrentPrice();
                } else {
                    totalPrice += price ;
                }

            }

        }
        double finalPrice = totalPrice - totalDiscount;
        formatted = formatDouble(totalPrice);
        sb.append(String.format(Locale.US, "  Total price: %s%n", formatted));
        formatted = formatDouble(totalDiscount);
        sb.append(String.format(Locale.US, "  Total discount: %s%n", formatted));
        formatted = formatDouble(finalPrice);
        sb.append(String.format(Locale.US, "  Final Price: %s", formatted));
        return sb.toString();
    }
    public static String formatDouble(double d) {
        String s = Double.toString(d);
        if (!s.contains(".")) {
            s += ".0";
        }
        return s;
    }
}

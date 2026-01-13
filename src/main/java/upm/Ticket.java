package upm;

import jakarta.persistence.*;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import upm.products.CustomizableProduct;
import upm.products.Product;
import upm.ticketitems.ServiceItem;
import upm.ticketitems.TicketItem;


import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

import static java.lang.Math.max;

@Entity
@Table(name = "tickets")
public class Ticket {

    @Id
    private String id; // PK de negocio, asignada por tu lógica

    @Transient
    private List<TicketItem> items = new ArrayList<>();
    private static final Comparator<TicketItem> ITEM_ORDER =
            Comparator.comparing(TicketItem::getClassStr)
                    .thenComparing(TicketItem::getItemId)
                    .thenComparing(TicketItem::getItemId);

    private TicketState currentState;
    private TicketType ticketType;

    private int productServiceSeparator = 0;

    public Ticket(String id, TicketType type) {
        this.id = (id == null) ? RandomGenerator.generateTicketId() : id;
        this.items = new ArrayList<>();
        this.currentState = TicketState.EMPTY;
        this.ticketType = type;
    }

    public void addItem(TicketItem ti) {
        //TODO: COMPROBAR EL TIPO DE TICKET

        if (currentState == TicketState.CLOSE) return;
        this.currentState = TicketState.OPEN;
        TicketItem tiOnList = getTicketItem(ti);

        if (tiOnList==null){
            items.add(ti);
            items.sort(ITEM_ORDER);

            if (ti.getClassStr().equals("Service")) productServiceSeparator +=1;
        }else {
            ti.addQuantity(ti.getQuantity());
        }
    }

    public TicketItem getTicketItem(TicketItem it) {
        for (TicketItem ticketItem : items) {
            if (ticketItem.getItemId().equals(it.getItemId())) {
                return ticketItem;
            }
        }

        return null;
    }


    public boolean removeProduct(int productId) {
        if (currentState != TicketState.CLOSE) {

            String id = Integer.toString(productId);
            boolean removed = items.removeIf(
                    item -> item.getItemId().equals(id)
            );

            if (removed) {
                return true;
            }

            System.out.println("El producto con ID " + productId + " no existe en el ticket.");
            return false;
        }

        return false;
    }


    public void closeAndPrint() {
        this.currentState = TicketState.CLOSE;

        ////////////////////////////////////////////////////////////////////////////////////
        DateTimeFormatter fixedFmt = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        LocalDateTime fixedDateTime = LocalDateTime.parse("25-12-07-22:32", fixedFmt);

        id = id + "-"+fixedDateTime.format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));
        ////////////////////////////////////////////////////////////////////////////////////

        //id = id + "-"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));
        System.out.println(this);
    }

    String getId() {
        return id;
    }

    TicketState getCurrentState() {
        return currentState;
    }

    TicketType getTicketType() {return ticketType;}

    List<TicketItem> getItems() {return items;}

    boolean hasServicesAndProducts(){
        return (productServiceSeparator != 0) && (productServiceSeparator!=items.size());
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ticket : ").append(id);

        double servicesDiscountPercent = 0;
        if (productServiceSeparator != 0){
            for (int i = 0; i < productServiceSeparator; i++) {
                servicesDiscountPercent+=15;
                sb.append("\n").append(items.get(i).toString());
            }
        }

        // Productos (si es COMPOUND mostramos el encabezado antes)

        double totalPrice = 0.0;
        double productDiscount = 0.0;

        if (productServiceSeparator != items.size()){
            sb.append("\nProduct Included");
            for (int i = productServiceSeparator; i < items.size(); i++) {
                TicketItem it = items.get(i);
                double price = it.getPrice();

                int total = items.stream()
                            .filter(item -> item.getItemId().equals(it.getItemId()))
                            .mapToInt(TicketItem::getQuantity)
                            .sum();


                if (total >= 2 && it.getCategory() != null) {
                    double discount = it.getCategory().getDiscountPercent() / 100.0;
                    double unitDiscount = price * discount;
                    String formatted = Utils.formatDouble(unitDiscount);
                    //if (prod instanceof CustomizableProduct cp){
                    //    for (int i = 0; i <it.getQuantity(); i++) {
                    //        //sb.append(String.format(Locale.US, "%n%s **discount -%s", cp.toString(it.getPersonalizedTexts(), price), formatted));
                    //        productDiscount += unitDiscount;
                    //    }
                    //}
                    for (int j = 0; j <it.getQuantity(); j++) {
                        sb.append(it.toString());
                        productDiscount += unitDiscount;
                    }


                } else {
                    sb.append(String.format(Locale.US, "%n%s", it));
                }

                totalPrice += it.getPrice();
            }
        }

        double servicesDiscount = totalPrice * servicesDiscountPercent/100f;

        // 3. FINAL SUMMARY
        if (ticketType == TicketType.PRODUCT || (ticketType == TicketType.COMPOUND && !items.isEmpty())) {
            double finalPrice = max(totalPrice - productDiscount - servicesDiscount, 0);

            String formatted = Utils.formatDouble(totalPrice);
            sb.append("\n").append(String.format(Locale.US, "  Total price: %s%n", formatted));

            if (servicesDiscountPercent != 0){
                formatted = Utils.formatDouble(servicesDiscount);
                sb.append(String.format(Locale.US, "Extra Discount from services:%s **discount -%s%n", formatted, formatted));
            }

            formatted = Utils.formatDouble(productDiscount+servicesDiscount);
            sb.append(String.format(Locale.US, "  Total discount: %s%n", formatted));

            formatted = Utils.formatDouble(finalPrice);
            sb.append(String.format(Locale.US, "  Final Price: %s", formatted));
        }

        return sb.toString();
    }
}

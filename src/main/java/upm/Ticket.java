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

    @Transient
    private static final Comparator<TicketItem> ITEM_ORDER =
            Comparator
                    .comparing((TicketItem t) -> !t.getItemId().endsWith("s"))
                    .thenComparing(TicketItem::getClassStr).reversed()
                    .thenComparing(TicketItem::getItemId).reversed()
                    .thenComparing(TicketItem::getItemId);

    @Enumerated(EnumType.STRING)
    private TicketState currentState;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    private int productServiceSeparator = 0;

    public Ticket(String id, TicketType type) {
        this.id = (id == null) ? RandomGenerator.generateTicketId() : id;
        this.items = new ArrayList<>();
        this.currentState = TicketState.EMPTY;
        this.ticketType = type;
    }
    protected Ticket() {
        // Constructor requerido por Hibernate
    }

    public void addItem(TicketItem ti) {
        boolean isService = ti.getClassStr().equals("Service");
        if ((isService && ticketType==TicketType.PRODUCT) || ((!isService) && ticketType==TicketType.SERVICE)) {
            Tienda.printError("El tipo de item que se desea añadir no es compatible con el ticket");
        }

        if (currentState == TicketState.CLOSE) return;
        this.currentState = TicketState.OPEN;
        TicketItem tiOnList = getTicketItem(ti);

        if (tiOnList==null || ti.getClassStr().equals(tiOnList.getClassStr())){
            items.add(ti);
            items.sort(ITEM_ORDER);

            if (isService) productServiceSeparator +=1;
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

            TicketItem it = items.stream()
                    .filter(i -> i.getItemId().equals(id))
                    .findFirst()
                    .orElse(null);

            boolean removed = items.removeIf(
                    item -> item.getItemId().equals(id)
            );

            if (it.getClassStr().equals("Service")) productServiceSeparator -=1;

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
            sb.append("\nServices Included:");

            for (int i = productServiceSeparator-1; i >= 0; i--) {
                servicesDiscountPercent+=15;
                sb.append("\n").append(items.get(i).toString());
            }
        }


        double ticketTotalPrice = 0.0;
        double productDiscount = 0.0;

        if (productServiceSeparator != items.size()){

            if (ticketType==TicketType.COMPOUND)
                sb.append("\nProduct Included");

            for (int i = productServiceSeparator; i < items.size(); i++) {
                TicketItem it = items.get(i);
                double priceOfAll = it.getPrice();

                int numberOfItems = items.stream()
                        .filter(item -> item.getItemId().equals(it.getItemId())).filter(item -> item.toString().equals(it.toString()) )
                        .mapToInt(TicketItem::getQuantity)
                        .sum();



                boolean multipleCustomizable = it.getClassStr().equals("CustomizableProduct") && items.stream()
                        .filter(item -> item.getItemId().equals(it.getItemId())).count() > 1;

                double pricePerItem = priceOfAll/numberOfItems;

                if ((numberOfItems >= 2 && it.getCategory() != null) || multipleCustomizable) {
                    double discount = it.getCategory().getDiscountPercent() / 100.0;
                    double unitDiscount = pricePerItem * discount;
                    String formatted = Utils.formatDouble(unitDiscount);

                    for (int j = 0; j <it.getQuantity(); j++) {
                        sb.append("\n").append(it.toString()).append(String.format(" **discount -%s", formatted));
                        productDiscount += unitDiscount;
                    }


                } else {
                    sb.append("\n").append(String.format(Locale.US, "%s", it));
                }

                ticketTotalPrice += priceOfAll;
            }
        }

        double servicesDiscount = ticketTotalPrice * servicesDiscountPercent/100f;

        // 3. FINAL SUMMARY
        if (ticketType == TicketType.PRODUCT || (ticketType == TicketType.COMPOUND && productServiceSeparator != items.size())) {
            double finalPrice = max(ticketTotalPrice - productDiscount - servicesDiscount, 0);

            String formatted = Utils.formatDouble(ticketTotalPrice);
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

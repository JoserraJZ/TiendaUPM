package upm;

import jakarta.persistence.*;

import upm.products.CustomizableProduct;
import upm.products.Product;
import upm.products.ProductCampusFood;
import upm.products.ProductMeeting;

import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static java.lang.Math.max;

@Entity
@Table(name = "tickets")
public class  Ticket {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Transient
    private final ArrayList<TicketItem> items ;
    @Transient
    private final ArrayList<ServiceItem> services;

    private TicketState currentState;
    private TicketType ticketType;

    @ManyToOne
    @JoinColumn(name = "cashier_id")
    private Cashier cashier;


    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public Ticket(String id, TicketType type) {
        this.id = (id == null) ? RandomGenerator.generateTicketId() : id;
        this.items = new ArrayList<>();
        this.services = new ArrayList<>();
        this.currentState = TicketState.EMPTY;
        this.ticketType = type;
    }

    public void addProducts(TicketItem ti, int amount) {
        if (currentState == TicketState.CLOSE) return;
        this.currentState = TicketState.OPEN;
        TicketItem tiOnList= getTicketItem(ti.getProduct());


        if (product instanceof ProductMeeting){
            if (ti!= null) {
               ti.addParticipantsPM(amount);
            }else{
                TicketItem tiNew= new TicketItem(product, 1, id);
                tiNew.addParticipantsPM(amount);
                items.add(tiNew);
            }
        } else if (product instanceof ProductCampusFood) {
            if (ti!= null) {
                ti.addParticipantsCF(amount);
            }else{
                TicketItem tiNew= new TicketItem(product, 1, id);
                tiNew.addParticipantsCF(amount);
                items.add(tiNew);
            }
        } else{
            if (ti!= null) {
                ti.updateCuantity(amount);
            }else{
                items.add(new TicketItem(product, amount, id));
            }

        }


    }

    public void addService(Service svc, int amount) {
        for (ServiceItem si : services) {
            if (si.getService().getId() == svc.getId()) {
                si.updateQuantity(amount);
                return;
            }
        }

        // Si no existe, lo añadimos nuevo
        services.add(new ServiceItem(svc, amount, this.id));
    }


    public TicketItem getTicketItem(TicketItem it) {

        TicketItem itemSelected= null;

        if (it.getProduct() instanceof CustomizableProduct cp){
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getProduct().getId()==cp.getId() && items.get(i).compareCustomizableProducts(it)){
                    itemSelected=items.get(i);
                }
            }

        }else {
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getProduct().equals(p)){
                    itemSelected=items.get(i);
                }
            }
        }

        return itemSelected;
    }


    public boolean removeProduct(int productId) {
        if (currentState != TicketState.CLOSE) {

            boolean removed = items.removeIf(
                    item -> item.getProduct().getId() == productId
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

    ArrayList<TicketItem> getItems() {return items;}

    ArrayList<ServiceItem> getServices() {return services;}




    @Override
    public String toString() {
        // Servicios ordenados y a listar sólo si existen
        services.sort(Comparator.comparing(ServiceItem::getId));

        StringBuilder sb = new StringBuilder("Ticket : ").append(id);

        double servicesDiscountPercent = 0;

        if (!services.isEmpty()) {
            sb.append("\nServices Included:");
            for (ServiceItem svc : services) {
                servicesDiscountPercent+=15;
                sb.append("\n").append(String.format(Locale.US, "%s", svc));
            }
        }

        // Productos (si es COMPOUND mostramos el encabezado antes)
        items.sort(
                Comparator.comparing(
                                (TicketItem item) -> item.getProduct().getCategory(),
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
                        .thenComparing(
                                item -> item.getProduct().getId(),
                                Comparator.nullsLast(Comparator.reverseOrder())
                        )
                        .thenComparing(
                                item -> item.getProduct().getPrice(),
                                Comparator.nullsLast(Comparator.naturalOrder())
                        )
        );


        double totalPrice = 0.0;
        double productDiscount = 0.0;

        if (ticketType == TicketType.COMPOUND && !items.isEmpty()) {
            sb.append("\nProduct Included");
        }

        // 2. PROCESS EACH PRODUCT
        for (TicketItem it : items) {

            Product prod = it.getProduct();
            double price = it.getProduct().getPrice();

            int total = items.stream()
                    .filter(item -> item.getProduct().getId() == it.getProduct().getId())
                    .mapToInt(TicketItem::getCuantity)
                    .sum();


            if (total >= 2 && it.getCategory() != null) {
                double discount = it.getCategory().getDiscountPercent() / 100.0;
                double unitDiscount = price * discount;
                String formatted = Utils.formatDouble(unitDiscount);
                for (int i = 0; i <it.getCuantity(); i++) {
                    sb.append(String.format(Locale.US, "%n%s **discount -%s", it.getProduct(), formatted));
                    productDiscount += unitDiscount;
                }

            } else {
                sb.append(String.format(Locale.US, "%n%s", it.getProduct()));
            }

            // Price calculation by product type
            if (prod instanceof ProductMeeting) {
                totalPrice += ((ProductMeeting) prod).calculateCurrentPrice();
            } else if (prod instanceof ProductCampusFood) {
                totalPrice += ((ProductCampusFood) prod).calculateCurrentPrice();
            } else {
                totalPrice += price*it.getCuantity();
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

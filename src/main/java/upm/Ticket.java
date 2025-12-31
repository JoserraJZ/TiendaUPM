package upm;

import jakarta.persistence.*;
import upm.products.Product;
import upm.products.ProductCampusFood;
import upm.products.ProductMeeting;

import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Entity
@Table(name = "ticket")
public class  Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    
    private final Map<Integer, ArrayList<Product>> items ;
    private final Map<Integer, ArrayList<Service>> services;

    private TicketState currentState;
    private TicketType ticketType;

    public Ticket(String id, TicketType type) {
        this.id = (id == null) ? RandomGenerator.generateTicketId() : id;
        this.items = new LinkedHashMap<>();
        this.services = new LinkedHashMap<>();
        this.currentState = TicketState.EMPTY;
        this.ticketType = type;
    }


    public void addProducts(Product product, int amount) {
        if (currentState == TicketState.CLOSE) return;
        this.currentState = TicketState.OPEN;
        ArrayList<Product> productList = items.getOrDefault(product.getId(), new ArrayList<>());

        if (product instanceof ProductMeeting || product instanceof ProductCampusFood){
            if (!productList.isEmpty()) productList.removeFirst();
            productList.add(product);
            //actualizar obtener productMeting igual y solo actualizar precio
        }
        else {
            productList.addAll(Collections.nCopies(amount, product));
        }

        items.put(product.getId(), productList);
    }

    public boolean removeProduct(int productId) {
        if (currentState != TicketState.CLOSE){
            if (items.remove(productId) != null) {
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

    Map getItems() {return items;}

    Map getServices() {return services;}



    @Override
    public String toString() {
        // Servicios ordenados y a listar sólo si existen
        List<Service> sortedServices = services.values().stream()
                .flatMap(List::stream)
                .sorted(Comparator.comparingInt(Service::getId))
                .collect(Collectors.toList());

        StringBuilder sb = new StringBuilder("Ticket : ").append(id);

        if (!sortedServices.isEmpty()) {
            sb.append("\nServices Included:");
            for (Service svc : sortedServices) {
                sb.append("\n").append(String.format(Locale.US, "%s", svc));
            }
        }

        // Productos (si es COMPOUND mostramos el encabezado antes)
        List<Product> sorted = items.values().stream()
                .flatMap(List::stream)
                .sorted(
                        Comparator.comparing((Product p) -> p.getClass().getSimpleName()).reversed()
                                .thenComparingInt(Product::getId).reversed()
                )
                .toList();

        Map<Integer, Long> counts =
                sorted.stream()
                        .collect(Collectors.groupingBy(Product::getId, Collectors.counting()));

        double totalPrice = 0.0;
        double totalDiscount = 0.0;

        if (ticketType == TicketType.COMPOUND && !items.isEmpty()) {
            sb.append("\nProduct Included");
        }

        // 2. PROCESS EACH PRODUCT
        for (Product prod : sorted) {

            double price = prod.getPrice();

            long occurrences = counts.get(prod.getId());

            if (occurrences >= 2 && prod.getCategory() != null) {
                double discount = prod.getCategory().getDiscountPercent() / 100.0;
                double unitDiscount = price * discount;
                String formatted = Utils.formatDouble(unitDiscount);

                sb.append(String.format(Locale.US, "%n%s **discount -%s", prod, formatted));
                totalDiscount += unitDiscount;

            } else {
                // NO DISCOUNT APPLIED
                sb.append(String.format(Locale.US, "%n%s", prod));
            }

            // Price calculation by product type
            if (prod instanceof ProductMeeting) {
                totalPrice += ((ProductMeeting) prod).calculateCurrentPrice();
            } else if (prod instanceof ProductCampusFood) {
                totalPrice += ((ProductCampusFood) prod).calculateCurrentPrice();
            } else {
                totalPrice += price;
            }
        }

        // 3. FINAL SUMMARY
        if (ticketType == TicketType.PRODUCT || (ticketType == TicketType.COMPOUND && !items.isEmpty())) {
            double finalPrice = totalPrice - totalDiscount;

            String formatted = Utils.formatDouble(totalPrice);
            sb.append("\n").append(String.format(Locale.US, "  Total price: %s%n", formatted));

            formatted = Utils.formatDouble(totalDiscount);
            sb.append(String.format(Locale.US, "  Total discount: %s%n", formatted));

            formatted = Utils.formatDouble(finalPrice);
            sb.append(String.format(Locale.US, "  Final Price: %s", formatted));
        }

        return sb.toString();
    }
}

package upm;

import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class Ticket {

    private final Map<Integer, ArrayList<Product>> items ;
    private String id;
    private TicketState currentState;

    public Ticket(String id) {
        this.id = (id == null) ? RandomGenerator.generateTicketId() : id;
        this.items = new LinkedHashMap<>();
        this.currentState = TicketState.EMPTY;
    }

    public void addProducts(Product product, int amount) {
        if (currentState == TicketState.CLOSE) return;
        this.currentState = TicketState.OPEN;
        ArrayList<Product> productList = items.getOrDefault(product.getId(), new ArrayList<>());

        if (product instanceof ProductMeeting || product instanceof ProductCampusFood){
            if (!productList.isEmpty()) productList.removeFirst();
            productList.add(product);
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
        id = id + "-"+LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));
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

        String formatted;
        StringBuilder sb = new StringBuilder("Ticket : ").append(id).append("\n");

        // 2. PROCESS EACH PRODUCT
        for (Product prod : sorted) {

            double price = prod.getPrice();

            long occurrences = counts.get(prod.getId());

            if (occurrences >= 2 && prod.getCategory() != null) {
                double discount = prod.getCategory().getDiscountPercent() / 100.0;
                double unitDiscount = price * discount;
                formatted = Utils.formatDouble(unitDiscount);

                sb.append(String.format(Locale.US, "%s **discount -%s%n", prod, formatted));
                totalDiscount += unitDiscount;

            } else {
                // NO DISCOUNT APPLIED
                sb.append(String.format(Locale.US, "%s%n", prod));
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
        double finalPrice = totalPrice - totalDiscount;

        formatted = Utils.formatDouble(totalPrice);
        sb.append(String.format(Locale.US, "  Total price: %s%n", formatted));

        formatted = Utils.formatDouble(totalDiscount);
        sb.append(String.format(Locale.US, "  Total discount: %s%n", formatted));

        formatted = Utils.formatDouble(finalPrice);
        sb.append(String.format(Locale.US, "  Final Price: %s", formatted));

        return sb.toString();
    }
}

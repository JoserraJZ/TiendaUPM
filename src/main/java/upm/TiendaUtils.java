package upm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class TiendaUtils {
    static void clientList(Set<Client> clients) {
        List<Client> clientList = new ArrayList<>(clients);
        clientList.sort(Comparator.comparing(Client::getName));

        System.out.println("Client:");
        clientList.forEach(System.out::println);
    }

    static void productList(Set<Cashier> cashiers) {
        List<Cashier> cashierList = new ArrayList<>(cashiers);
        cashierList.sort(Comparator.comparing(Cashier::getName));

        System.out.println("Cash:");
        cashierList.forEach(System.out::println);
    }

    static ClientType getClientTypeFromId(String id) {
        boolean isDNI = id.matches("\\d{8}[A-Za-z]");
        boolean isNIE = id.matches("[XY]\\d{7}[A-Za-z]");
        boolean isNIF = id.matches("[A-Za-z]\\d{8}");
        ClientType type = isNIF ? ClientType.COMPANY : ClientType.USER;
        return type;
    }

    public static TicketType getTicketTypeFromParams(String[] params) {
        TicketType type = TicketType.PRODUCT; // default
        if (params.length > 2 && params[3] != null) {
            String t = params[3].toLowerCase();
            type = switch (t) {
                case "c", "-c", "compound" -> TicketType.COMPOUND;
                case "p", "-p", "product" -> TicketType.PRODUCT;
                case "s", "-s", "service" -> TicketType.SERVICE;
                default -> TicketType.PRODUCT;
            };
        }
        return type;
    }
}

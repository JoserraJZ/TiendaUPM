package upm;

import upm.products.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class TiendaUtils {

    public static void clientAdd(String[] params, Set<Client> clients, Set<Cashier> cashiers) {
        String id = params[1];
        boolean isDNI = id.matches("\\d{8}[A-Za-z]");
        boolean isNIE = id.matches("[XY]\\d{7}[A-Za-z]");
        boolean isNIF = id.matches("[A-Za-z]\\d{8}");
        Cashier cash = Tienda.getCashierById(params[3]);
        ClientType type = isNIF ? ClientType.COMPANY : ClientType.USER;


        if (cash != null) {
            if (!(isDNI || isNIE || isNIF)) {
                Tienda.printError("Identificador de usuario no válido");
            } else {
                Client newClient;
                if (isNIF) {
                    newClient = new ClientCompany(params[0], params[1], params[2], cash, type);
                } else {
                    newClient = new Client(params[0], params[1], params[2], cash, type);
                }
                if (!clients.add(newClient))
                    Tienda.printError("El identificador de usuario introducido ya existe");
                //else {h.addClientToDb(newClient);}
            }
        } else Tienda.printError("El identificador de cajero introducido no existe");
    }

    static void clientList(Set<Client> clients) {
        List<Client> clientList = new ArrayList<>(clients);
        clientList.sort(Comparator.comparing(Client::getName));

        System.out.println("Client:");
        clientList.forEach(System.out::println);
    }

    public static void prodUpdate (String[] params, Catalog<Product> productCatalog) {
        Product prod = productCatalog.getById(Integer.parseInt(params[0]));
        if (prod != null) {
            switch (params[1]) {
                case "NAME":
                    prod.setName(params[2]);
                    break;
                case "CATEGORY":
                    prod.setCategory(ProductCategory.valueOf(params[2]));
                    break;
                case "PRICE":
                    prod.setPrice(Integer.parseInt(params[2]));
                    break;
                default:
                    Tienda.printError("Atributo de producto desconocido");
            }
            System.out.println(prod);
        } else {
            Tienda.printError("Atributo de producto desconocido");
        }
    }

    public static void addFood(String[] params, Catalog<Product> productCatalog) {
        //CAMBIAR FECHA, DE FIXEDDATETIME A NOW()

        Product prod = null;
        LocalDateTime expiration =LocalDate.parse(params[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();

        ////////////////////////////////////////////////////////////////////////////////////
        DateTimeFormatter fixedFmt = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        LocalDateTime fixedDateTime = LocalDateTime.parse("25-12-07-22:32", fixedFmt);
        ///////////////////////////////////////////////////////////////////////////////////

        if (Integer.parseInt(params[4]) <= 100 && !(Duration.between(fixedDateTime, expiration).toDays() < 3)) {
            prod = new ProductCampusFood(
                    params[0],
                    params[1],
                    Double.parseDouble(params[2]),
                    expiration,
                    Integer.parseInt(params[4]));
            productCatalog.add(prod.getId(), prod);

            //addProductToDb(prod);
        }
        if (prod == null)
            Tienda.printError("Error processing ->prod addFood ->Error adding product");
        else System.out.println(prod);
    }

    static void productList(Set<Cashier> cashiers) {
        List<Cashier> cashierList = new ArrayList<>(cashiers);
        cashierList.sort(Comparator.comparing(Cashier::getName));

        System.out.println("Cash:");
        cashierList.forEach(System.out::println);
    }

    public static void ticketList(Set<Cashier> cashiers) {
        System.out.println("Ticket List:");

        for (Cashier c : cashiers) {
            List<Ticket> list = new ArrayList<>(c.getTickets());

            list.sort(Comparator.comparing(Ticket::getCurrentState).thenComparing(Ticket::getId, Comparator.reverseOrder() ));

            for (Ticket t : list) {
                System.out.println(t.getId() + " - " + t.getCurrentState());
            }
        }
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

    public static Product prodAdd(String[] params, Catalog<Product> productCatalog) {
        Product prod;
        if (params[4] != null && Integer.parseInt(params[4]) > 0) {
            prod = new CustomizableProduct(
                    params[0],
                    params[1],
                    ProductCategory.valueOf(params[2]),
                    Integer.parseInt(params[3]),
                    Integer.parseInt(params[4]));
            productCatalog.add(prod.getId(), prod);

            //h.addProductToDb(prod);
        } else {
            prod = new Product(
                    params[0],
                    params[1],
                    ProductCategory.valueOf(params[2]),
                    Integer.parseInt(params[3]));
            productCatalog.add(prod.getId(), prod);

            //h.addProductToDb(prod);
        }
        return prod;
    }

    public static void printTicket(Ticket ticket) {
        if (ticket != null) {
            if (ticket.getTicketType() == TicketType.COMPOUND) {
                if (ticket.hasServicesAndProducts()) {
                    ticket.closeAndPrint();
                } else {
                    Tienda.printError("Un ticket mixto debe contener al menos un producto y un servicio");
                }
            } else if (ticket.getTicketType() == TicketType.PRODUCT) {
                ticket.closeAndPrint();
            } else if (ticket.getTicketType() == TicketType.SERVICE) {
                ticket.closeAndPrint();
            }
        }
    }

    public static void addMeeting(String[] params, Catalog<Product> productCatalog) {
        ProductMeeting prod = null;
        LocalDateTime expiration = LocalDate.parse(params[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();

        //CAMBIAR FECHA, DE FIXEDDATETIME A NOW()
        ///////////////////////////////////////////////////////////////////////////////////
        DateTimeFormatter fixedFmt = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        LocalDateTime fixedDateTime = LocalDateTime.parse("25-12-07-22:32", fixedFmt);
        ///////////////////////////////////////////////////////////////////////////////////

        if (Integer.parseInt(params[4]) <= 100 && !(Duration.between(fixedDateTime, expiration).toHours() < 12)) {
            prod = new ProductMeeting(
                    params[0],
                    params[1],
                    Double.parseDouble(params[2]),
                    expiration,
                    Integer.parseInt(params[4]),
                    fixedDateTime);
            productCatalog.add(prod.getId(), prod);

            //h.addProductToDb(prod);
        }
        if (prod == null)
            Tienda.printError("Error processing ->prod addMeeting ->Error adding meeting");
        else System.out.println(prod);
        //else System.out.println(prod.toNoPeopleString());
    }
}

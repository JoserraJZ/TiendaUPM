package upm;

import upm.products.*;
import upm.ticketitems.ProductItem;
import upm.ticketitems.ServiceItem;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class TiendaUtils {

    public static void clientAdd(String name, String identifier, String email, String cashId, Set<Client> clients) {

        IdentifierType idType = Utils.identifyType(identifier);
        if (idType == IdentifierType.UNIDENTIFIED) {
            Tienda.printError("Identificador de usuario no válido");
            return;
        }

        Cashier cash = Tienda.getCashierById(cashId);
        if (cash == null) {
            Tienda.printError("El identificador de cajero introducido no existe");
            return;
        }

        Client newClient = (idType == IdentifierType.NIF)
                ? new ClientCompany(name, identifier, email, cash)
                : new Client(name, identifier, email, cash);

        if (!clients.add(newClient))
            Tienda.printError("El identificador de usuario introducido ya existe");
    }

    static void clientList(Set<Client> clients) {
        List<Client> clientList = new ArrayList<>(clients);
        clientList.sort(Comparator.comparing(Client::getName));

        System.out.println("Client:");
        clientList.forEach(System.out::println);
    }

    public static void prodUpdate (String productId, String tochangeParameter, String newValue, Catalog<Product> productCatalog) {
        Product prod = productCatalog.getById(Integer.parseInt(productId));

        if (prod==null){
            Tienda.printError("Atributo de producto desconocido");
            return;
        }

        switch (tochangeParameter) {
            case "NAME":
                prod.setName(newValue);
                break;
            case "CATEGORY":
                prod.setCategory(ProductCategory.valueOf(newValue));
                break;
            case "PRICE":
                prod.setPrice(Integer.parseInt(newValue));
                break;
            default:
                Tienda.printError("Atributo de producto desconocido");
        }
        System.out.println(prod);
    }

    public static void addCampusFood(String id, String name, String price, String expirationDate, String maxParticipants, Catalog<Product> productCatalog) {
        int maxParticipantsInt = Integer.parseInt(maxParticipants);
        if (maxParticipantsInt > 100){
            Tienda.printError("Error processing ->prod addFood ->Error adding product");
            return;
        }

        LocalDateTime expiration = LocalDate.parse(expirationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        //TODO: CAMBIAR FECHA, DE FIXEDDATETIME A NOW()
        LocalDateTime creationDateTime = LocalDateTime.parse("25-12-07-22:32", DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));

        if (Duration.between(creationDateTime, expiration).toHours() < 12){
            Tienda.printError("La duración entre la expiración y la creación es menos de 12 horas");
            return;
        }
        ProductCampusFood prod = new ProductCampusFood(
                id,
                name,
                Double.parseDouble(price),
                expiration,
                Integer.parseInt(maxParticipants)
        );

        if (!productCatalog.add(prod.getId(), prod))
            Tienda.printError("Ha habido un error al añadir el campus food");
        else System.out.println(prod);
    }

    public static void addMeeting(String id, String name, String price, String expirationDate, String maxParticipants, Catalog<Product> productCatalog) {
        int maxParticipantsInt = Integer.parseInt(maxParticipants);
        if (maxParticipantsInt > 100){
            Tienda.printError("No se pueden añadir más de 100 participantes");
            return;
        }

        LocalDateTime expiration = LocalDate.parse(expirationDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        //TODO: CAMBIAR FECHA, DE FIXEDDATETIME A NOW()
        LocalDateTime creationDateTime = LocalDateTime.parse("25-12-07-22:32", DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));

        if (Duration.between(creationDateTime, expiration).toHours() < 12){
            Tienda.printError("La duración entre la expiración y la creación es menos de 12 horas");
            return;
        }

        ProductMeeting prod = new ProductMeeting(
                id,
                name,
                Double.parseDouble(price),
                expiration,
                Integer.parseInt(maxParticipants)
        );

        if (!productCatalog.add(prod.getId(), prod))
            Tienda.printError("Ha habido un error al añadir el meeting");
        else System.out.println(prod);
    }

    static void productList(Set<Cashier> cashiers) {
        List<Cashier> cashierList = new ArrayList<>(cashiers);
        cashierList.sort(Comparator.comparing(Cashier::getName));

        System.out.println("Cash:");
        cashierList.forEach(System.out::println);
    }

    public static void ticketAdd(Ticket ticket, String productIdStr, String amountStr, List<String> personalizedTexts, Catalog<Product> productCatalog, Catalog<Service> servicesCatalog) {
        if (ticket == null) {
            Tienda.printError("El identificador del ticket introducido no existe");
            return;
        }

        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            amount = 1;
        }

        //TODO:CAMBIAR FECHA, DE FIXEDDATETIME A NOW()
        LocalDateTime currentDateTime = LocalDateTime.parse("25-12-07-22:32", DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm"));

        if (!productIdStr.toLowerCase().endsWith("s")) {
            Product prod = productCatalog.getById(Integer.parseInt(productIdStr));

            if (prod == null) {
                Tienda.printError("el producto con id: "+productIdStr+" no existe");
                return;
            }


            if (!personalizedTexts.isEmpty()) {
                CustomizableProduct customProd = ((CustomizableProduct) prod.clone());
                personalizedTexts.forEach(customProd::addText);
                ticket.addItem(new ProductItem(customProd,amount));

            } else {
                if (prod instanceof ProductMeeting prodM) {
                    if (prodM.getExpirationDateTime().isAfter(currentDateTime) ||
                            prodM.getExpirationDateTime().isEqual(currentDateTime)) {

                        ticket.addItem(new ProductItem(prodM, amount));
                    } else {
                        Tienda.printError("La reunion que se está tratando de añadir ha prescrito");
                    }

                } else if (prod instanceof ProductCampusFood prodCF) {
                    if (prodCF.getExpirationDate().isAfter(currentDateTime) ||
                            prodCF.getExpirationDate().isEqual(currentDateTime)) {

                        ticket.addItem(new ProductItem(prodCF, amount));
                    } else {
                        Tienda.printError("La comida que se está tratando de añadir ha prescrito");
                    }

                } else {
                    ticket.addItem(new ProductItem(prod,amount));
                }

            }
        }
        else{

            int serviceId;
            try {
                serviceId = Integer.parseInt(productIdStr.replaceAll("\\D+", ""));
            } catch (NumberFormatException e) {
                Tienda.printError("ID de servicio inválido");
                return;
            }

            Service svc = servicesCatalog.getById(serviceId);
            if (svc == null) {
                Tienda.printError("Servicio no encontrado");
                return;
            }

            Service servicetoAdd = svc.clone();

            LocalDateTime svcExpiry = servicetoAdd.getExpirationDate().toLocalDateTime();
            if (svcExpiry.isBefore(currentDateTime)) {
                Tienda.printError("El servicio que se está tratando de añadir ha prescrito");
                return;
            }

            if (ticket.getTicketType() == TicketType.PRODUCT) {
                Tienda.printError("El ticket no admite servicios");
                return;
            }

            ticket.addItem(new ServiceItem(svc));

        }
        System.out.println(ticket);
    }

    public static void ticketRemove(Ticket ticket, String productId){
        if (ticket == null) {
            Tienda.printError("El identificador del ticket introducido no existe");
            return;
        }

        if (ticket.removeProduct(Integer.parseInt(productId)))
            System.out.println(ticket);
        else Tienda.printError("Error eliminando el ticket");
    }

    public static void ticketList(Set<Cashier> cashiers) {
        System.out.println("Ticket List:");

        for (Cashier c : cashiers) {
            List<Ticket> list = new ArrayList<>(c.getTickets());

            list.sort(Comparator.comparing(Ticket::getCurrentState,
                            Comparator.comparingInt(s -> s == TicketState.EMPTY ? 0
                                    : s == TicketState.OPEN ? 1 : 2))
                    .thenComparing(Ticket::getId, Comparator.reverseOrder()));

            for (Ticket t : list) {
                System.out.println(t.getId() + " - " + t.getCurrentState());
            }
        }
    }


    public static TicketType getTicketTypeFromParams(String paramsStr) {
        if (paramsStr == null)
            return TicketType.PRODUCT;

        TicketType type = switch (paramsStr.toLowerCase()) {
            case "c", "-c", "compound" -> TicketType.COMPOUND;
            case "p", "-p", "product" -> TicketType.PRODUCT;
            case "s", "-s", "service" -> TicketType.SERVICE;
            default -> null;
        };
        if (type == null)
            Tienda.printError("The ticket type "+paramsStr+" wasn't recognized");

        return type;
    }

    public static void prodAdd(String productId, String name, String category, String price, String maxPersonalization, Catalog<Product> productCatalog) {

        Product prod =
                (maxPersonalization != null && Integer.parseInt(maxPersonalization) > 0)
                ? new CustomizableProduct(
                        productId,
                        name,
                        ProductCategory.valueOf(category),
                        Integer.parseInt(price),
                        Integer.parseInt(maxPersonalization))
                : new Product(
                        productId,
                        name,
                        ProductCategory.valueOf(category),
                        Integer.parseInt(price));


        if (!productCatalog.add(prod.getId(), prod))
            Tienda.printError("No se pueden añadir más de 200 productos");
        else
            System.out.println(prod);
    }

    public static void printTicket(Ticket ticket) {
        if (ticket == null) {
            Tienda.printError("El identificador del ticket introducido no existe");
            return;
        }

        if (ticket.getTicketType() == TicketType.COMPOUND && !ticket.hasServicesAndProducts())
                Tienda.printError("Un ticket mixto debe contener al menos un producto y un servicio");
        else
            ticket.closeAndPrint();
    }


}
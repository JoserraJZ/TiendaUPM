package upm;
import upm.products.*;
import upm.ticketitems.ProductItem;
import upm.ticketitems.ServiceItem;
import upm.ticketitems.TicketItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Tienda {

    private static final Catalog<Product> productCatalog = new Catalog<>(200);
    private static final Catalog<Service> servicesCatalog = new Catalog<>();

    private static final Set<Cashier>  cashiers = new TreeSet<>();
    private static final Set<Client>    clients = new HashSet<>();

    private static boolean exit = false;
    private static boolean errorOccurred = false;
    private static HibernateUtils h;

    public static void main(String[] args) {
        h = new HibernateUtils();

        System.out.println("Welcome to the ticket module App.\n" + "Ticket module. Type 'help' to see commands.");

        RandomGenerator.Init(productCatalog, cashiers, clients);

        boolean shouldRunFromFile = args.length > 0 && Utils.runFromFile(args[0]);

        if (!shouldRunFromFile) {
            try (Scanner scanner = new Scanner(System.in)) {
                commandLoop(scanner);
            }
        }

        System.out.println("Closing application.\nGoodbye!");
        h.endConnection();
    }

    public static void commandLoop(Scanner scanner){
        while (scanner.hasNextLine()) {
            try {
                errorOccurred = false;
                if (executeCommand(scanner.nextLine())) {
                    break; // salir si executeCommand devuelve true
                }
            } catch (Exception e) {
                System.err.println("Se ha dado el error: " + e.getMessage());
            }
        }
    }

    private static boolean executeCommand(String rawInput) {
        System.out.print("\ntUPM> ");

        ValidatedCommand validatedCommand = Command.validateCommand(rawInput);
        if(validatedCommand==null)return false;

        Command command = validatedCommand.command;
        String[] params = validatedCommand.parameters;

        if ("true".equals(System.getProperty("isfromfile")))
            System.out.println(rawInput);

        switch (command){
            case CASH_ADD -> {
                if(!cashiers.add(new Cashier(params[0], params[1], params[2])))
                    printError("El id introducido ya existe");
            }
            case CASH_LIST -> {
                TiendaUtils.productList(cashiers);
            }
            case CASH_REMOVE -> {
                if (!cashiers.remove(getCashierById(params[0])))
                    printError("No se ha encontrado ningún cajero con el identificador introducido");
            }
            case CASH_TICKETS -> {
                Cashier cash = getCashierById(params[0]);
                cash.listTicket();
            }

            case CLIENT_ADD -> {
                TiendaUtils.clientAdd(params, clients, cashiers);
            }
            case CLIENT_LIST -> {
                TiendaUtils.clientList(clients);
            }
            case CLIENT_REMOVE -> {
                if (!clients.removeIf(c -> params[0].equals(c.getId())))
                    printError("No se ha encontrado ningún cliente con el identificador introducido");
            }

            case PROD_ADD -> {
                Product prod = TiendaUtils.prodAdd(params, productCatalog);
                if (prod == null) printError("No se pueden añadir más de 200 productos");
                else System.out.println(prod);
            }
            case PROD_ADD_ALT_SERVICE -> {
                LocalDateTime date = LocalDate.parse(params[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                Service service = new Service(RandomGenerator.generateServiceId(), date, params[1]);
                servicesCatalog.add(service.getId(), service);
                //h.addServiceToDb(service);
                System.out.println(service);
            }
            case PROD_UPDATE -> {
                TiendaUtils.prodUpdate(params, productCatalog);
            }
            case PROD_ADDFOOD -> {
                TiendaUtils.addFood(params, productCatalog);
            }
            case PROD_ADDMEETING -> {
                TiendaUtils.addMeeting(params, productCatalog);
            }
            case PROD_LIST -> productCatalog.list();
            case PROD_REMOVE -> {
                Product prod = productCatalog.remove(Integer.parseInt(params[0]));

                if (prod != null) System.out.println(prod);
                else printError("Producto no encontrado");
            }

            case TICKET_NEW -> {
                //comprobar si el tipo de ticket que se quiere se puede para el tipo de cliente
                TicketType ticketType = TiendaUtils.getTicketTypeFromParams(params);
                Ticket nuevo = new Ticket(params[0], ticketType);
                getCashierById(params[1]).addTicket(nuevo);
                //h.addTicketToDb(nuevo);
                System.out.println(nuevo);
            }
            case TICKET_ADD -> {
                TiendaUtils.ticketAdd(params, productCatalog, servicesCatalog, cashiers);
            }
            case TICKET_REMOVE -> {
                Ticket ticket = getTicketById(params[1], params[0]);

                if (ticket.removeProduct(Integer.parseInt(params[2])))
                    System.out.println(ticket);
                else printError("Error eliminando el ticket");
            }
            case TICKET_PRINT -> {
                //modificar toString de Ticket para cada tipo de ticket
                TiendaUtils.printTicket(getTicketById(params[1], params[0]));
            }
            case TICKET_LIST -> {
                TiendaUtils.ticketList(cashiers);
            }

            case HELP -> help();
            case ECHO -> System.out.println(rawInput.substring(5));
            case EXIT -> exit = true;

        }
        if (!errorOccurred && (command.commandText.contains("prod") || command.commandText.contains("ticket") ||
                command.commandText.contains("cash") || command.commandText.contains("client"))) {
            System.out.println(command.commandText + ": ok");
        }
        return exit;
    }

    public static Ticket getTicketById(String cashId, String id) {
        for (Ticket t : getCashierById(cashId).getTickets()) {
            if (id.equals(t.getId())) {
                return t;
            }
        }
        printError("No se ha encontrado ningún ticket con el identificador introducido");
        return null;
    }


    public static Cashier getCashierById(String id) {
        for (Cashier c : cashiers) {
            if (id.equals(c.getId())) {
                return c;
            }
        }
        printError("No se ha encontrado ningún cajero con el identificador introducido");
        return null;
    }

    public static void printError(String message){
        System.out.println(message);
        errorOccurred =true;
    }

    private static void help() {
        System.out.println("Commands:");

        System.out.println(Command.getAllCommandsHelp());

        System.out.println("""
                Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS
                Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.""");
    }
}
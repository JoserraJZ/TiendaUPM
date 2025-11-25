package main.java.upm;
import javax.security.auth.callback.CallbackHandler;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Tienda {

    //private Ticket currentTicket;        ////?
    private final ProductCatalog catalog; /////?
    private final Set<Cashier> cashiers;
    private final Set<Client> clients;
    private final Set<Ticket> tickets;

    public static void main(String[] args) {
        System.out.println("Welcome to the ticket module App.\n" +
                "Ticket module. Type 'help' to see commands.");

        Tienda store = new Tienda();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                if (store.executeCommand(scanner)) {
                    break; // exit loop if executeCommand returns true
                }
            } catch (Exception e) {
                System.err.println("Se ha dado el error: " + e.getMessage());
            }
        }
        System.out.println("Closing application.\nGoodbye!");
    }

    public Tienda() {
        this.catalog = new ProductCatalog();
        this.cashiers = new HashSet<>();
        this.clients = new HashSet<>();
        this.tickets = new HashSet<>();

        RandomGenerator.Init(catalog, cashiers, clients);
    }

    private boolean executeCommand(Scanner scanner) {
        System.out.print("\ntUPM> ");
        String inputCommand = scanner.nextLine();

        boolean exit = false;

        ValidatedCommand commandAndParams = Command.validateCommand(Command.splitCommand(inputCommand));
        if(commandAndParams==null)return false;

        Command command = commandAndParams.command;
        String[] commandParameters = commandAndParams.parameters;

        boolean correcto = true;

        switch (command){
            //CASHIER COMMANDS
            case CASH_ADD -> {
                if(!cashiers.add(new Cashier(commandParameters[0], commandParameters[1], commandParameters[2]))){
                    System.out.println("El id introducido ya existe");
                }
            }
            case CASH_LIST -> {
                List<Cashier> cashierList = new ArrayList<>(cashiers);
                cashierList.sort(Comparator.comparing(Cashier::getName));//cashierlist.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));

                System.out.println("Cash:");
                cashierList.forEach(System.out::println);
            }
            case CASH_REMOVE -> {
                if (!cashiers.removeIf(c -> commandParameters[0].equals(c.getId()))){
                    System.out.println("No se ha encontrado ningún cajero con el identificador introducido");
                }
            }
            case CASH_TICKETS -> {
                Cashier cash = findCashierById(commandParameters[0]);
                if (cash != null) {
                    // Obtener colección de tickets desde el cajero
                    List<Ticket> tickets = new ArrayList<>(cash.getTickets());
                    // Ordenar por Id (asumiendo Id entero)
                    // tickets.sort(Comparator.comparingInt(Ticket::getId));
                    System.out.println("Tickets: ");
                    tickets.forEach(t -> System.out.println(t.getId() + " ->" + t.getCurrentState()));
                } else {
                    System.out.println("El identificador de cajero introducido no existe");
                }
            }
            //CLIENT COMMANDS
            case CLIENT_ADD -> {
                Cashier cash = findCashierById(commandParameters[3]);
                if (cash != null){
                    if (!clients.add(new Client(commandParameters[0],
                            commandParameters[1],
                            commandParameters[2],
                            cash))){
                        System.out.println("El identificador de usuario introducido ya existe");
                    }
                }else{
                    System.out.println("El identificador de cajero introducido no existe");
                }
            }
            case CLIENT_LIST -> {
                List<Client> clientList = new ArrayList<>(clients);
                clientList.sort(Comparator.comparing(Client::getName)); //clientList.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));


                System.out.println("Client:");
                clientList.forEach(System.out::println);
            }
            case CLIENT_REMOVE -> {
                if (!clients.removeIf(c -> commandParameters[0].equals(c.getId()))){
                    System.out.println("No se ha encontrado ningún cliente con el identificador introducido");
                }
            }
            //PRODUCT COMMANDS
            case PROD_ADD -> {
                //TODO: TEMPORAL, CAMBIAR Y AÑADIR MAXPERS

                Product prod;
                if (commandParameters[4] != null){
                    //PRODUCTO PERSONALIZADO (creo que esta bien pero no lo se seguro)
                    prod = catalog.add(new CustomizableProduct(
                            commandParameters[0],
                            commandParameters[1],
                            ProductCategory.valueOf(commandParameters[2]),
                            Integer.parseInt(commandParameters[3]),
                            Integer.parseInt(commandParameters[4])
                    ));
                } else {
                    prod = catalog.add(new Product(
                            commandParameters[0],
                            commandParameters[1],
                            ProductCategory.valueOf(commandParameters[2]),
                            Integer.parseInt(commandParameters[3])
                    ));
                }
                if (prod == null) {
                    System.out.println("No se pueden añadir más de 200 productos");
                } else {
                    System.out.println(prod);
                }
            }
            case PROD_UPDATE -> {
                boolean done = catalog.update(Integer.parseInt(commandParameters[0]), commandParameters[1], commandParameters[2]);
                if (!done){
                    System.out.println("Atributo de producto desconocido");
                }
            }
            // Pongo los de addfood y addmeeting como creo que serán
            case PROD_ADDFOOD -> {
                Product prod = null;
                if (Integer.parseInt(commandParameters[4]) <= 100) {
                    prod = catalog.add(new ProductCampusFood(
                            commandParameters[0],
                            commandParameters[1],
                            Double.parseDouble(commandParameters[2]),
                            LocalDate.parse(commandParameters[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(),
                            Integer.parseInt(commandParameters[4])
                    ));
                } else {
                    System.out.println("Error processing ->prod addFood ->Error adding product");
                    correcto = false;
                }
                if (prod == null) {
                    //System.out.println("No se pueden añadir más de 200 productos");
                } else {
                    System.out.println(prod);
                }
            }
            case PROD_ADDMEETING -> {
                Product prod = null;
                if (Integer.parseInt(commandParameters[4]) <= 100) {
                    prod = catalog.add(new ProductMeeting(
                            commandParameters[0],
                            commandParameters[1],
                            Double.parseDouble(commandParameters[2]),
                            LocalDate.parse(commandParameters[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(),
                            Integer.parseInt(commandParameters[4]),
                            LocalDateTime.now()
                    ));
                } else {
                    System.out.println("Error processing ->prod addMeeting ->Error adding meeting");
                    correcto = false;
                }
                if (prod == null) {
                    //System.out.println("No se pueden añadir más de 200 productos");
                } else {
                    System.out.println(prod);
                }
            }
            case PROD_LIST -> catalog.list();
            case PROD_REMOVE -> {
                Product prod = catalog.remove(Integer.parseInt(commandParameters[0]));
                if (prod != null) {
                    System.out.println(prod);
                } else {
                    System.out.println("Producto no encontrado");
                }
            }
            //TICKET COMMANDS
            case TICKET_NEW -> {
                String id = commandParameters[0];      // puede ser null si no se pasa
                String cashId = commandParameters[1];
                String userId = commandParameters[2];
                Ticket nuevo = new Ticket(id, cashId, userId);

                Cashier cajero = null;
                for (Cashier c : cashiers) {
                    if (c.getId().equals(commandParameters[1])) {
                        cajero = c;
                    }
                }
                this.tickets.add(nuevo);
                cajero.addTicket(nuevo);
                //this.tickets.add(nuevo);
                System.out.println(nuevo);
            }
            //COMPROBAR ADD Y REMOVE
            case TICKET_ADD -> {
                if (catalog.getById(Integer.parseInt(commandParameters[2])) != null) {

                    if (commandParameters.length>4){
                        CustomizableProduct pPersonalizado= ((CustomizableProduct) catalog.getById(Integer.parseInt(commandParameters[2]))).clone();
                        for (int i = 4; i < commandParameters.length; i++) {
                            pPersonalizado.addPersonalizedText(commandParameters[i]);
                        }
                        getTicketById(commandParameters[0]).addProducts(pPersonalizado, Integer.parseInt(commandParameters[3]));
                    }else {
                        Product pNew = catalog.getById(Integer.parseInt(commandParameters[2]));
                        if (pNew instanceof ProductMeeting) {
                            ProductMeeting pMNew = (ProductMeeting) pNew;
                            pMNew.addParticipants(Integer.parseInt(commandParameters[3]));
                            getTicketById(commandParameters[0]).addProducts(pMNew, 1);
                        } else if (pNew instanceof CustomizableProduct) {
                            CustomizableProduct pPersonalizado = ((CustomizableProduct) catalog.getById(Integer.parseInt(commandParameters[2]))).clone();
                            getTicketById(commandParameters[0]).addProducts(pPersonalizado, Integer.parseInt(commandParameters[3]));
                        } else {
                            getTicketById(commandParameters[0]).addProducts(
                                    catalog.getById(Integer.parseInt(commandParameters[2])),
                                    Integer.parseInt(commandParameters[3])
                            );
                        }
                    }
                } else {
                    System.out.println("Producto no encontrado en catálogo");
                }
                getTicketById(commandParameters[0]).printTicketNoClose();
            }

            case TICKET_REMOVE -> {
                boolean removed = getTicketById(commandParameters[0]).removeProduct(Integer.parseInt(commandParameters[2]));
                if (removed) {
                    getTicketById(commandParameters[0]).printTicketNoClose();
                }
            }
            case TICKET_PRINT -> getTicketById(commandParameters[0]).printTicket();
            case TICKET_LIST -> {
                System.out.println("Ticket List:");

                List<Ticket> list = new ArrayList<>(tickets);
                list.sort(Comparator.comparing(Ticket::getCashId).
                        thenComparing(Ticket::getCurrentState));

                for (Ticket t : list) {
                    System.out.println(t.getId() + " - " + t.getCurrentState());
                }
            }
            //GENERAL COMMANDS
            case HELP -> this.help();
            case ECHO -> System.out.println(inputCommand.substring(5));
            case EXIT -> exit = true;

        }
        if ((command.commandText.contains("prod") || command.commandText.contains("ticket") ||
                command.commandText.contains("cash") || command.commandText.contains("client"))&& correcto) {
            System.out.println(command.commandText + ": ok");
        }

        return exit;
    }

    public Ticket getTicketById(String id) {
        if (id == null) return null;
        for (Ticket t : tickets) {
            if (id.equals(t.getId())) {
                return t;
            }
        }
        return null;
    }

    private void help() {
        System.out.println("Commands:");
        for (Command command : Command.values()) {
            System.out.println(" " + command.getHelp());
        }

        System.out.println("\nCategories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS\n" +
                "Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.");
    }
    public Cashier findCashierById(String id) {
        if (id == null) return null;

        for (Cashier c : cashiers) {
            if (id.equals(c.getId())) {
                return c;
            }
        }
        return null;
    }
}
package upm;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

//TODO: PORQUE TIENDA ES UNA INSTANCIA???
public class Tienda {

    private final ProductCatalog catalog;
    private final Set<Cashier> cashiers;
    private final Set<Client> clients;

    private boolean exit = false;
    private boolean errorOcurred = false;

    public static void main(String[] args) {
        System.out.println("Welcome to the ticket module App.\n" + "Ticket module. Type 'help' to see commands.");
        Tienda store = new Tienda();

        boolean shouldRunFromFile = args.length > 0 && store.runFromFile(args[0]);

        if (!shouldRunFromFile) {
            try (Scanner scanner = new Scanner(System.in)) {
                store.commandLoop(scanner);
            }
        }

        System.out.println("Closing application.\nGoodbye!");
    }

    private boolean runFromFile(String filePath) {
        File inputFile = new File(filePath);

        if (!inputFile.isFile()){
            System.err.println("Fichero no encontrado: " + filePath + ". Ejecutando modo interactivo.");
            return false;
        }

        System.setProperty("isfromfile", "true");


        try (Scanner fileScanner = new Scanner(inputFile)) {
            commandLoop(fileScanner);
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("Error abriendo el fichero: " + e.getMessage());
            return false;
        } finally {
            System.clearProperty("isfromfile");
        }
    }

    private void commandLoop(Scanner scanner){
        while (scanner.hasNextLine()) {
            try {
                this.errorOcurred = false;
                if (this.executeCommand(scanner.nextLine())) {
                    break; // salir si executeCommand devuelve true
                }
            } catch (Exception e) {
                System.err.println("Se ha dado el error: " + e.getMessage());
            }
        }
    }

    public Tienda() {
        this.catalog = new ProductCatalog();
        this.cashiers = new TreeSet<>();
        this.clients = new HashSet<>();

        RandomGenerator.Init(catalog, cashiers, clients);
    }

    private boolean executeCommand(String rawInput) {
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
                List<Cashier> cashierList = new ArrayList<>(cashiers);
                cashierList.sort(Comparator.comparing(Cashier::getName));

                System.out.println("Cash:");
                cashierList.forEach(System.out::println);
            }
            case CASH_REMOVE -> {
                if (!cashiers.remove(getCashierById(params[0])))
                    printError("No se ha encontrado ningún cajero con el identificador introducido");
            }
            case CASH_TICKETS -> {
                Cashier cash = getCashierById(params[0]);
                if (cash != null) {
                    List<Ticket> tickets = new ArrayList<>(cash.getTickets());
                    tickets.sort(Comparator.comparing(Ticket::getId));

                    System.out.println("Tickets: ");
                    tickets.forEach(t -> System.out.println(t.getId() + " ->" + t.getCurrentState()));
                }
                else printError("El identificador de cajero introducido no existe");
            }

            case CLIENT_ADD -> {
                Cashier cash = getCashierById(params[3]);
                if (cash != null){
                    if (!clients.add(new Client(params[0], params[1], params[2], cash)))
                        printError("El identificador de usuario introducido ya existe");
                }
                else printError("El identificador de cajero introducido no existe");
            }
            case CLIENT_LIST -> {
                List<Client> clientList = new ArrayList<>(clients);
                clientList.sort(Comparator.comparing(Client::getName));

                System.out.println("Client:");
                clientList.forEach(System.out::println);
            }
            case CLIENT_REMOVE -> {
                if (!clients.removeIf(c -> params[0].equals(c.getId())))
                    printError("No se ha encontrado ningún cliente con el identificador introducido");
            }

            case PROD_ADD -> {
                Product prod;
                if (params[4] != null && Integer.parseInt(params[4])>0){
                    prod = catalog.add(new CustomizableProduct(
                            params[0],
                            params[1],
                            ProductCategory.valueOf(params[2]),
                            Integer.parseInt(params[3]),
                            Integer.parseInt(params[4])
                    ));
                } else {
                    prod = catalog.add(new Product(
                            params[0],
                            params[1],
                            ProductCategory.valueOf(params[2]),
                            Integer.parseInt(params[3])
                    ));
                }
                if (prod == null) printError("No se pueden añadir más de 200 productos");
                else System.out.println(prod);
            }
            case PROD_ADD_ALT_SERVICE -> {
                //TODO: COMANDO ALTERNATIVO AÑADIR SERVICIO
            }
            case PROD_UPDATE -> {
                boolean done = catalog.update(Integer.parseInt(params[0]), params[1], params[2]);
                if (!done) printError("Atributo de producto desconocido");
            }
            case PROD_ADDFOOD -> {
                Product prod = null;
                LocalDateTime expiration =LocalDate.parse(params[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();

                if (Integer.parseInt(params[4]) <= 100 && !(Duration.between(LocalDateTime.now(), expiration).toDays() < 3)) {
                    prod = catalog.add(new ProductCampusFood(
                            params[0],
                            params[1],
                            Double.parseDouble(params[2]),
                            expiration,
                            Integer.parseInt(params[4])
                    ));
                }
                if (prod == null)
                    printError("Error processing ->prod addFood ->Error adding product");
                else System.out.println(prod);
            }
            case PROD_ADDMEETING -> {
                Product prod = null;
                LocalDateTime expiration =LocalDate.parse(params[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();

                if (Integer.parseInt(params[4]) <= 100 && !(Duration.between(LocalDateTime.now(), expiration).toHours() < 12)) {
                    prod = catalog.add(new ProductMeeting(
                            params[0],
                            params[1],
                            Double.parseDouble(params[2]),
                            expiration,
                            Integer.parseInt(params[4]),
                            LocalDateTime.now()
                    ));
                }
                if (prod == null)
                    printError("Error processing ->prod addMeeting ->Error adding meeting");
                else System.out.println(prod);
            }
            case PROD_LIST -> catalog.list();
            case PROD_REMOVE -> {
                Product prod = catalog.remove(Integer.parseInt(params[0]));

                if (prod != null) System.out.println(prod);
                else printError("Producto no encontrado");
            }

            case TICKET_NEW -> {
                Ticket nuevo = new Ticket(params[0]);
                getCashierById(params[1]).addTicket(nuevo);

                System.out.println(nuevo);
            }
            case TICKET_ADD -> {
                int productId = Integer.parseInt(params[2]);
                Product prod = catalog.getById(productId);

                if (prod != null) {
                    int amount = Integer.parseInt(params[3]);
                    Ticket ticket = getTicketById(params[1], params[0]);

                    if (params.length>4){
                        CustomizableProduct pPersonalizado= ((CustomizableProduct) prod).clone();
                        for (int i = 4; i < params.length; i++) {
                            pPersonalizado.addPersonalizedText(params[i]);
                        }
                        ticket.addProducts(pPersonalizado, amount);
                    }
                    else {
                        if (prod instanceof ProductMeeting prodM) {
                            if ( prodM.getExpirationDateTime().isAfter(LocalDateTime.now()) ||
                                    prodM.getExpirationDateTime().isEqual(LocalDateTime.now())){
                                prodM.addParticipants(amount);
                                ticket.addProducts(prod, amount);
                            }else {
                                printError("La reunion que se está tratando de añadir ha prescrito");
                            }

                        } else if (prod instanceof ProductCampusFood prodCF) {
                            if ( prodCF.getExpirationDate().isAfter(LocalDateTime.now()) ||
                                    prodCF.getExpirationDate().isEqual(LocalDateTime.now())){
                                prodCF.addParticipants(amount);
                                ticket.addProducts(prod, amount);
                            }else {
                                printError("La comida que se está tratando de añadir ha prescrito");
                            }

                        } else {
                            prod = prod.clone();
                            ticket.addProducts(prod, amount);
                        }

                    }
                    System.out.println(ticket);
                }
                else printError("Producto no encontrado en catálogo");
            }

            case TICKET_REMOVE -> {
                Ticket ticket = getTicketById(params[1], params[0]);

                if (ticket.removeProduct(Integer.parseInt(params[2])))
                    System.out.println(ticket);
                else printError("Error eliminando el ticket");
            }
            case TICKET_PRINT -> {
                Ticket ticket = getTicketById(params[1], params[0]);
                if (ticket != null) {
                    ticket.closeAndPrint();
                }
            }
            case TICKET_LIST -> {
                System.out.println("Ticket List:");

                for (Cashier c : cashiers) {
                    List<Ticket> list = new ArrayList<>(c.getTickets());

                    list.sort(Comparator.comparing(Ticket::getCurrentState).thenComparing(Ticket::getId));

                    for (Ticket t : list) {
                        System.out.println(t.getId() + " - " + t.getCurrentState());
                    }
                }
            }

            case HELP -> this.help();
            case ECHO -> System.out.println(rawInput.substring(5));
            case EXIT -> exit = true;

        }
        if (!errorOcurred && (command.commandText.contains("prod") || command.commandText.contains("ticket") ||
                command.commandText.contains("cash") || command.commandText.contains("client"))) {
            System.out.println(command.commandText + ": ok");
        }
        return exit;
    }

    public Ticket getTicketById(String cashId, String id) {
        for (Ticket t : getCashierById(cashId).getTickets()) {
            if (id.equals(t.getId())) {
                return t;
            }
        }
        printError("No se ha encontrado ningún ticket con el identificador introducido");
        return null;
    }


    public Cashier getCashierById(String id) {
        for (Cashier c : cashiers) {
            if (id.equals(c.getId())) {
                return c;
            }
        }
        printError("No se ha encontrado ningún cajero con el identificador introducido");
        return null;
    }

    public void printError(String message){
        System.out.println(message);
        this.errorOcurred =true;
    }

    private void help() {
        System.out.println("Commands:");

        System.out.println(Command.getAllCommandsHelp());

        System.out.println("""
                Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS
                Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.""");
    }
}
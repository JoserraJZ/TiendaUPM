package upm;
import upm.products.*;

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

        boolean shouldRunFromFile = args.length > 0 && runFromFile(args[0]);

        if (!shouldRunFromFile) {
            try (Scanner scanner = new Scanner(System.in)) {
                commandLoop(scanner);
            }
        }

        System.out.println("Closing application.\nGoodbye!");
       h.endConnection();
    }

    private static boolean runFromFile(String filePath) {
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

    private static void commandLoop(Scanner scanner){
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
                else {h.addCashierToDb(getCashierById(params[0]));}

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
            /*
            case CLIENT_ADD -> {
                boolean isCompany = Utils.isNIF(params[1]);
                //System.out.println(isCompany);
                Cashier cash = getCashierById(params[3]);
                if (cash != null){
                    if (!clients.add(new Client(params[0], params[1], params[2], cash)))
                        printError("El identificador de usuario introducido ya existe");
                }
                else printError("El identificador de cajero introducido no existe");
            }

             */

            case CLIENT_ADD -> {
                String id = params[1];
                boolean isDNI = id.matches("\\d{8}[A-Za-z]");
                boolean isNIE = id.matches("[XY]\\d{7}[A-Za-z]");
                boolean isNIF = id.matches("[A-Za-z]\\d{8}");
                Cashier cash = getCashierById(params[3]);
                ClientType type = isNIF ? ClientType.COMPANY : ClientType.USER;

                if (cash != null) {
                    if (!(isDNI || isNIE || isNIF)) {
                        printError("Identificador de usuario no válido");
                    } else {
                        Client newClient;
                        if (isNIF) {
                            newClient = new ClientCompany(params[0], params[1], params[2], cash, type);
                        } else {
                            newClient = new Client(params[0], params[1], params[2], cash, type);
                        }
                        if (!clients.add(newClient))
                            printError("El identificador de usuario introducido ya existe");
                        else {h.addClientToDb(newClient);}
                    }
                } else printError("El identificador de cajero introducido no existe");
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
                    prod =  new CustomizableProduct(
                            params[0],
                            params[1],
                            ProductCategory.valueOf(params[2]),
                            Integer.parseInt(params[3]),
                            Integer.parseInt(params[4]));
                    productCatalog.add(prod.getId(), prod);

                    h.addProductToDb(prod);
                } else {
                    prod = new Product(
                            params[0],
                            params[1],
                            ProductCategory.valueOf(params[2]),
                            Integer.parseInt(params[3]));
                    productCatalog.add(prod.getId(), prod);

                    h.addProductToDb(prod);
                }
                if (prod == null) printError("No se pueden añadir más de 200 productos");
                else System.out.println(prod);
            }
            case PROD_ADD_ALT_SERVICE -> {
                LocalDateTime date = LocalDate.parse(params[0], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
                Service service = new Service(RandomGenerator.generateServiceId(), date, params[1]);
                servicesCatalog.add(-1, service);
                h.addServiceToDb(service);
                System.out.println(service);
            }
            case PROD_UPDATE -> {
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
                            return false;
                    }
                    System.out.println(prod);
                } else {
                    printError("Atributo de producto desconocido");
                }
            }
            case PROD_ADDFOOD -> {
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

                    h.addProductToDb(prod);
                }
                if (prod == null)
                    printError("Error processing ->prod addFood ->Error adding product");
                else System.out.println(prod);
            }
            case PROD_ADDMEETING -> {
                //CAMBIAR FECHA, DE FIXEDDATETIME A NOW()

                Product prod = null;
                LocalDateTime expiration =LocalDate.parse(params[3], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();

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

                    h.addProductToDb(prod);
                }
                if (prod == null)
                    printError("Error processing ->prod addMeeting ->Error adding meeting");
                else System.out.println(prod);
            }
            case PROD_LIST -> productCatalog.list();
            case PROD_REMOVE -> {
                Product prod = productCatalog.remove(Integer.parseInt(params[0]));

                if (prod != null) System.out.println(prod);
                else printError("Producto no encontrado");
            }

            case TICKET_NEW -> {

                //comprobar si el tipo de ticket que se quiere se puede para el tipo de cliente

                TicketType tipoTicket = TicketType.PRODUCT; // default
                if (params.length > 2 && params[3] != null) {
                    String t = params[3].toLowerCase();
                    tipoTicket = switch (t) {
                        case "c", "-c", "compound" -> TicketType.COMPOUND;
                        case "p", "-p", "product" -> TicketType.PRODUCT;
                        case "s", "-s", "service" -> TicketType.SERVICE;
                        default -> TicketType.PRODUCT;
                    };
                }


                Ticket nuevo = new Ticket(params[0], tipoTicket);

                getCashierById(params[1]).addTicket(nuevo);
                nuevo.setCashier(getCashierById(params[1]));
                h.addTicketToDb(nuevo);
                System.out.println(nuevo);
            }
            case TICKET_ADD -> {
                if (!params[2].toLowerCase().endsWith("s")) {
                    //CAMBIAR FECHA, DE FIXEDDATETIME A NOW()

                    // COMPROBAR SI EL TICKET ES DE PRODUCTO, SERVICIO O MIXTO

                    ///////////////////////////////////////////////////////////////////////////////////
                    DateTimeFormatter fixedFmt = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
                    LocalDateTime fixedDateTime = LocalDateTime.parse("25-12-07-22:32", fixedFmt);
                    ///////////////////////////////////////////////////////////////////////////////////

                    int productId = Integer.parseInt(params[2]);
                    Product prod = productCatalog.getById(productId);

                    if (prod != null) {
                        int amount = Integer.parseInt(params[3]);
                        Ticket ticket = getTicketById(params[1], params[0]);
                        if (params.length > 4) {
                            TicketItem itemToAdd = new TicketItem(prod,amount, ticket.getId());

                            CustomizableProduct pPersonalizado = ((CustomizableProduct) prod);
                            for (int i = 4; i < params.length; i++) {
                                itemToAdd.addPersonalizedText(params[i]);
                            }
                            //ticket.addProducts(pPersonalizado, amount);
                        } else {
                            if (prod instanceof ProductMeeting prodM) {
                                if (prodM.getExpirationDateTime().isAfter(fixedDateTime) ||
                                        prodM.getExpirationDateTime().isEqual(fixedDateTime)) {
                                    ProductMeeting meetingToAdd = prodM.clone();
                                    //ticket.addProducts(meetingToAdd, amount);
                                } else {
                                    printError("La reunion que se está tratando de añadir ha prescrito");
                                }

                            } else if (prod instanceof ProductCampusFood prodCF) {
                                if (prodCF.getExpirationDate().isAfter(fixedDateTime) ||
                                        prodCF.getExpirationDate().isEqual(fixedDateTime)) {
                                    ProductCampusFood campusFoodToAdd = prodCF.clone();
                                    //ticket.addProducts(campusFoodToAdd, amount);
                                } else {
                                    printError("La comida que se está tratando de añadir ha prescrito");
                                }

                            } else {
                                //ticket.addProducts(prod.clone(), amount);
                            }

                        }
                        System.out.println(ticket);
                    } else printError("Producto no encontrado en catálogo");
                }
                else{

                    DateTimeFormatter fixedFmt = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
                    LocalDateTime fixedDateTime = LocalDateTime.parse("25-12-07-22:32", fixedFmt);

                    // params: [0]=ticketId, [1]=cashId, [2]=serviceId (ej. "1S"), [3]=amount (opcional)
                    String svcIdRaw = params[2];
                    int serviceId;
                    try {
                        serviceId = Integer.parseInt(svcIdRaw.replaceAll("\\D+", ""));
                    } catch (NumberFormatException e) {
                        printError("ID de servicio inválido");
                        break;
                    }

                    Service svc = servicesCatalog.getById(serviceId);
                    if (svc == null) {
                        printError("Servicio no encontrado");
                        break;
                    }
                    Service servicetoAdd = svc.cloneService();

                    int amount = 1;
                    if (params.length > 3 && params[3] != null && params[3].matches("^[1-9]\\d*$")) {
                        amount = Integer.parseInt(params[3]);
                    }

                    Ticket ticket = getTicketById(params[1], params[0]); // (cashId, ticketId)
                    if (ticket == null) break;

                    LocalDateTime svcExpiry = servicetoAdd.getExpirationDate().toLocalDateTime();
                    if (svcExpiry.isBefore(fixedDateTime)) {
                        printError("El servicio que se está tratando de añadir ha prescrito");
                        break;
                    }

                    // Solo tickets de tipo SERVICE o COMPOUND admiten servicios
                    if (ticket.getTicketType() == TicketType.PRODUCT) {
                        printError("El ticket no admite servicios");
                        break;
                    }

                    // Forzar apertura del ticket (currentState es privado en Ticket)
                    try {
                        java.lang.reflect.Field f = Ticket.class.getDeclaredField("currentState");
                        f.setAccessible(true);
                        f.set(ticket, TicketState.OPEN);
                    } catch (Exception ignored) {
                    }

                    //@SuppressWarnings("unchecked")
                    ticket.addService(servicetoAdd,1);

                    System.out.println(ticket);
                }
            }
            case TICKET_REMOVE -> {
                Ticket ticket = getTicketById(params[1], params[0]);

                if (ticket.removeProduct(Integer.parseInt(params[2])))
                    System.out.println(ticket);
                else printError("Error eliminando el ticket");
            }
            case TICKET_PRINT -> {
                //modificar toString de Ticket para cada tipo de ticket
                Ticket ticket = getTicketById(params[1], params[0]);
                if (ticket != null) {
                    if (ticket.getTicketType() == TicketType.COMPOUND) {
                        if ((!ticket.getItems().isEmpty()) && (!ticket.getServices().isEmpty())) {
                            ticket.closeAndPrint();
                        } else {
                            printError("Un ticket mixto debe contener al menos un producto y un servicio");
                        }
                    } else if (ticket.getTicketType() == TicketType.PRODUCT) {
                        ticket.closeAndPrint();
                    } else if (ticket.getTicketType() == TicketType.SERVICE) {
                        ticket.closeAndPrint();
                    }
                }
            }
            case TICKET_LIST -> {
                System.out.println("Ticket List:");

                for (Cashier c : cashiers) {
                    List<Ticket> list = new ArrayList<>(c.getTickets());

                    list.sort(Comparator.comparing(Ticket::getCurrentState).thenComparing(Ticket::getId, Comparator.reverseOrder() ));

                    for (Ticket t : list) {
                        System.out.println(t.getId() + " - " + t.getCurrentState());
                    }
                }
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
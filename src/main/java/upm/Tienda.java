package upm;
import java.time.LocalDateTime;
import java.util.*;

public class Tienda {

    //private Ticket currentTicket;        ////?
    private final ProductCatalog catalog; /////?
    private final Set<Cashier> cashiers;
    private final Set<Client> clients;

    public static void main(String[] args) {
        System.out.println("Welcome to the ticket module App.\n" +
                "Ticket module. Type 'help' to see commands.");

        Tienda store = new Tienda();
        Scanner scanner = new Scanner(System.in);

        while (!store.executeCommand(scanner));

        System.out.println("Closing application.\nGoodbye!");
    }

    public Tienda() {
        this.catalog = new ProductCatalog();
        this.cashiers = new HashSet<>();
        this.clients = new HashSet<>();
    }

    private boolean executeCommand(Scanner scanner) {
        System.out.print("\ntUPM> ");
        String inputCommand = scanner.nextLine();

        boolean exit = false;

        ValidatedCommand commandAndParams = Command.validateCommand(Command.splitCommand(inputCommand));
        if(commandAndParams==null)return false;

        Command command = commandAndParams.command;
        String[] commandParameters = commandAndParams.parameters;

        switch (command){
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
            case PROD_ADD -> {
                //TODO: TEMPORAL, CAMBIAR Y AÑADIR MAXPERS
                int id = (int)(Math.random()*10000);
                try{id = Integer.parseInt(commandParameters[0]);} catch (NumberFormatException ignored) {}

                Product prod;
                if (commandParameters[3] != null){
                    //PRODUCTO PERSONALIZADO (creo que esta bien pero no lo se seguro)
                    prod = catalog.add(new CustomizableProduct(
                            id,
                            commandParameters[1],
                            ProductCategory.valueOf(commandParameters[2]),
                            Integer.parseInt(commandParameters[3]),
                            Integer.parseInt(commandParameters[4])
                    ));
                } else {
                    prod = catalog.add(new Product(
                            id,
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
                int id = (int)(Math.random()*10000);
                try{id = Integer.parseInt(commandParameters[0]);} catch (NumberFormatException ignored) {}

                Product prod;

                prod = catalog.add(new ProductCampusFood(
                        id,
                        commandParameters[1],
                        Integer.parseInt(commandParameters[2]),
                        LocalDateTime.parse(commandParameters[3]),
                        Integer.parseInt(commandParameters[4])
                ));

                if (prod == null) {
                    System.out.println("No se pueden añadir más de 200 productos");
                } else {
                    System.out.println(prod);
                }
            }
            case PROD_ADDMEETING -> {
                int id = (int)(Math.random()*10000);
                try{id = Integer.parseInt(commandParameters[0]);} catch (NumberFormatException ignored) {}

                Product prod;

                prod = catalog.add(new ProductMeeting(
                        id,
                        commandParameters[1],
                        Integer.parseInt(commandParameters[2]),
                        LocalDateTime.parse(commandParameters[3]),
                        Integer.parseInt(commandParameters[4]),
                        LocalDateTime.now()
                ));

                if (prod == null) {
                    System.out.println("No se pueden añadir más de 200 productos");
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

            /*
            case TICKET_NEW -> this.currentTicket = new Ticket(0);
            case TICKET_ADD -> {
                if (catalog.getById(Integer.parseInt(commandParameters[2])) != null) {
                    currentTicket.addProducts(catalog.getById(Integer.parseInt(commandParameters[2])), Integer.parseInt(commandParameters[3]));
                } else {
                    System.out.println("Producto no encontrado en catálogo");
                }
                currentTicket.printTicket();
            }
            case TICKET_REMOVE -> this.currentTicket.removeProduct(Integer.parseInt(commandParameters[2]));
            case TICKET_PRINT -> currentTicket.printTicket();

             */
            case HELP -> this.help();
            case ECHO -> System.out.println(inputCommand.substring(5));
            case EXIT -> exit = true;


        }
        if (command.commandText.contains("prod") || command.commandText.contains("ticket") ||
                command.commandText.contains("cash") || command.commandText.contains("client")){
            System.out.println(command.commandText + ": ok");
        }

        return exit;
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

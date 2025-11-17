package upm;
import java.util.*;

public class Tienda {

    //private Ticket currentTicket;        ////?
    private ProductCatalog catalog; /////?
    private Map<Integer, Cashier> cajeros;
    ///
    /// Crear lista o array de int con los ids registrados de tickets?

    //crear un objeto productCatalog?

    public static void main(String[] args) {
        System.out.println("Welcome to the ticket module App.\n" +
                "Ticket module. Type 'help' to see commands.");

        Tienda store = new Tienda();
        Scanner scanner = new Scanner(System.in);

        while (!store.executeCommand(scanner));

        System.out.println("Closing application.\nGoodbye!");
    }

    public Tienda() {
        this.currentTicket = new Ticket(22222);
        this.catalog= new ProductCatalog();
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
            case PROD_ADD -> {
                //TODO: TEMPORAL, CAMBIAR Y AÑADIR MAXPERS
                int id = (int)(Math.random()*10000);
                try{id = Integer.parseInt(commandParameters[0]);} catch (NumberFormatException ignored) {}

                Product prod = catalog.add(new Product(
                        id,
                        commandParameters[1],
                        ProductCategory.valueOf(commandParameters[2]),
                        Integer.parseInt(commandParameters[3])
                ));

                if (prod == null) {
                    System.out.println("No se pueden añadir más de 200 productos");
                } else {
                    System.out.println(prod);
                }
            }
            case PROD_LIST -> catalog.list();
            case PROD_UPDATE -> {
                boolean done = catalog.update(Integer.parseInt(commandParameters[0]), commandParameters[1], commandParameters[2]);
                if (!done){
                    System.out.println("Atributo de producto desconocido");
                }
            }
            case PROD_REMOVE -> {
                Product prod = catalog.remove(Integer.parseInt(commandParameters[0]));
                if (prod != null) {
                    System.out.println(prod);
                } else {
                    System.out.println("Producto no encontrado");
                }
            }

            case TICKET_NEW -> this.currentTicket = new Ticket(0);
            //case TICKET_ADD -> {
            //    if (catalog.getById(Integer.parseInt(args[2])) != null) {
            //        currentTicket.addProducts(catalog.getById(Integer.parseInt(args[2])), Integer.parseInt(args[3]));
            //    } else {
            //        System.out.println("Producto no encontrado en catálogo");
            //    }
            //    currentTicket.printTicket();
            //}
            //case TICKET_REMOVE -> this.currentTicket.removeProduct(Integer.parseInt(args[2]));
            case TICKET_PRINT -> currentTicket.printTicket();

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

}

package upm;

import java.util.Scanner;

public class Tienda {

    private Ticket ticketActual;        ////?
    private ProductCatalog catalog; /////?

    //crear un objeto productCatalog?

    public static void main(String[] args) {
        Tienda st1 = new Tienda();
        Scanner sc = new Scanner(System.in);
        boolean exit=false;
        System.out.print("Welcome to the ticket module App.\n" +
                "Ticket module. Type 'help' to see commands.\n");
        do {
            System.out.print("tUPM> ");
            exit= st1.executeCommand(sc);
        }while (!exit);

    }

    public Tienda() {
        this.ticketActual = new Ticket();
        this.catalog= new ProductCatalog();
    }

    private boolean executeCommand(Scanner scanner) {
        String mainCommand = scanner.nextLine();
        String[] atributes = mainCommand.split(" ");
        /// validador*******
        boolean exit = false;
        switch (atributes[0]) {
            case "prod":
                switch (atributes[1]) {
                    case "add":
                        catalog.add(new Product(Integer.parseInt(atributes[2]), atributes[3], Category.valueOf(atributes[4]),Double.parseDouble(atributes[5]) ));
                        break;
                    case "list":
                        catalog.list();
                        break;
                    case "update":
                        switch (atributes[3]){
                            case "nombre":catalog.update(Integer.parseInt(atributes[2]),"nombre", atributes[4]);
                                break;
                            case "categoria":catalog.update(Integer.parseInt(atributes[2]),"categoria", atributes[4]);
                                break;
                            case "precio":catalog.update(Integer.parseInt(atributes[2]),"precio", atributes[4]);
                                break;
                        }
                        break;
                    case "remove":
                        catalog.remove(Integer.parseInt(atributes[2]));
                        break;
                    default:
                        System.out.println("Comando prod desconocido.");
                }
                break;
            case "ticket":
                switch (atributes[1]) {
                    case "new":
                        this.ticketActual= new Ticket();
                        break;
                    case "add":
                        if (catalog.getById(Integer.parseInt(atributes[2]))!=null){
                            ticketActual.add(catalog.getById(Integer.parseInt(atributes[2])), Integer.parseInt(atributes[3]));
                        }
                        break;
                    case "remove":
                        this.ticketActual.ticketRemove(Integer.parseInt(atributes[1]));
                        break;
                    case "print":
                        ticketActual.ticketPrint();
                        break;
                    default:
                        System.out.println("Comando ticket desconocido.");
                }
                break;
            case "help":
                this.help();
                break;
            case "echo":
                this.echo(mainCommand.substring(5));
                break;
            case "exit":
                exit = true;
                break;
            default:
                System.out.println("Comando desconocido.");
        }
        return exit;
    }

    private void help() {
        System.out.println("Commands:");
        for (CommandNames command : CommandNames.values()) {
            System.out.println("  " + command.getHelp());
        }

        System.out.println();
        System.out.println("Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
        System.out.println("Discounts if there are ≥2 units in the category:");
        System.out.println("  MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.");
    }

    public void echo(String text) {
        if (text == null) {
            System.out.println("Uso: echo \"<texto>\"");
            return;
        }
        System.out.println("echo "+text);
    }







}

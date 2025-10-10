package main.java.upm;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Tienda {

    private Ticket ticketActual;        ////?
    private ProductCatalog catalog; /////?

    //crear un objeto productCatalog?

    public static void main(String[] args) {
        Tienda st1 = new Tienda();
        Scanner sc = new Scanner(System.in);
        boolean exit=false;
        do {
            System.out.print("tUPM> ");
            exit= st1.ejecutarComando(sc);
        }while (!exit);

    }

    public Tienda() {
        this.productsMap = new HashMap<>();
        this.cantidadProductos = 0;
        this.catalog= new ProductCatalog();
    }

    private boolean ejecutarComando(Scanner scanner) {
        String mainCommand = scanner.nextLine();
        String[]atributos= mainCommand.split(" ");
        boolean exit = false;
        switch (atributos[0]) {
            case "prod":
                switch (atributos [1]) {
                    case "add":
                        catalog.add(new Product(Integer.parseInt(atributos[2]), atributos[3], Category.valueOf(atributos[4]),Double.parseDouble(atributos[5]) ));
                        break;
                    case "list":
                        catalog.list();
                        break;
                    case "update":
                        switch (atributos[3]){
                            case "nombre":catalog.update(Integer.parseInt(atributos[2]),"nombre", atributos[4]);
                                break;
                            case "categoria":catalog.update(Integer.parseInt(atributos[2]),"categoria", atributos[4]);
                                break;
                            case "precio":catalog.update(Integer.parseInt(atributos[2]),"precio", atributos[4]);
                                break;
                        }
                        break;
                    case "remove":
                        catalog.remove(Integer.parseInt(atributos[2]));
                        break;
                    default:
                        System.out.println("Comando prod desconocido.");
                }
                break;
            case "ticket":
                String ticketCommand = scanner.next();
                switch (atributos[1]) {
                    case "new":
                        this.ticketActual= new Ticket();
                        break;
                    case "add":
                        ticketActual.add(new Product());
                        break;
                    case "remove":
                        this.ticketRemove(scanner);
                        break;
                    case "print":
                        this.ticketPrint();
                        break;
                    default:
                        System.out.println("Comando ticket desconocido.");
                }
                break;
            case "help":
                this.help();
                break;
            case "echo":
                this.echo(scanner);
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

    private void echo(Scanner scanner){
        if (scanner.hasNextLine()) {
            String text = scanner.nextLine().trim();
            if (text.startsWith("\"") && text.endsWith("\"")) {
                text = text.substring(1, text.length() - 1);
            }
            System.out.println("echo \"" + text + "\"");
            System.out.println(text);
        } else {
            System.out.println("Uso: echo \"<texto>\"");
        }
    }





}

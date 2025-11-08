package upm;
import java.util.*;
import java.util.regex.*;

public class Tienda {

    private Ticket ticketActual;        ////?
    private ProductCatalog catalog; /////?
    ///
    /// Crear lista o array de int con los ids registrados de tickets?

    //crear un objeto productCatalog?

    public static void main(String[] args) {
        Tienda st1 = new Tienda();
        Scanner sc = new Scanner(System.in);
        boolean exit=false;
        System.out.print("Welcome to the ticket module App.\n" +
                "Ticket module. Type 'help' to see commands.\n");
        do {
            exit = st1.executeCommand(sc);
        } while (!exit);

    }

    public Tienda() {
        this.ticketActual = new Ticket(000000);
        this.catalog= new ProductCatalog();
    }

    private boolean executeCommand(Scanner scanner) {
        String mainCommand = scanner.nextLine();
        System.out.println("tUPM> " + mainCommand);
        String[] atributes = splitCommand(mainCommand);
        /// validador*******
        boolean exit = false;
        try{

            switch (atributes[0]) {
                case "prod":
                    if(atributes.length>1){
                        switch (atributes[1]) {
                            case "add":
                                if(atributes.length==6) {
                                    Product p = catalog.add(new Product(Integer.parseInt(atributes[2]), atributes[3], Category.valueOf(atributes[4]), Integer.parseInt(atributes[5])));

                                    if(p== null){
                                        System.out.println("No se pueden añadir más de 200 productos");
                                    }else{
                                        System.out.printf(Locale.US, "{class:%s, id:%d, name:'%s', category:%s, price:%.1f}%n",
                                                "Product", p.getIdProducto(), p.getNombreProducto(), p.getCat().toString(), p.getPrecio());
                                        System.out.println("prod add: ok");
                                    }
                                    break;
                                }else System.out.println("Comando prod desconocido.");break;
                            case "list":
                                catalog.list();
                                break;
                            case "update":
                                if(atributes.length==5) {
                                    switch (atributes[3]){
                                        case "NAME":catalog.update(Integer.parseInt(atributes[2]),"nombre", atributes[4]);
                                            break;
                                        case "CATEGORY":catalog.update(Integer.parseInt(atributes[2]),"categoria", atributes[4]);
                                            break;
                                        case "PRICE":catalog.update(Integer.parseInt(atributes[2]),"precio", atributes[4]);
                                            break;
                                    }
                                    System.out.println("prod update: ok");
                                    break;
                                }else System.out.println("Comando prod desconocido.");break;
                            case "remove":
                                if(atributes.length==3) {
                                    Product prod = catalog.remove(Integer.parseInt(atributes[2]));
                                    System.out.printf(Locale.US,"{class:%s, id:%d, name:'%s', category:%s, price:%.1f}%n",
                                            "Product", prod.getIdProducto(), prod.getNombreProducto(), prod.getCat().toString(), prod.getPrecio());
                                    System.out.println("prod remove: ok");
                                    break;
                                }else System.out.println("Comando prod desconocido.");break;
                            default:
                                System.out.println("Comando prod desconocido.");
                        }
                        break;
                    }else System.out.println("Comando prod desconocido.");break;
                case "ticket":
                    if(atributes.length>1) {
                        switch (atributes[1]) {
                            case "new":
                                this.ticketActual = new Ticket(000000);//CAAAAAMBIARLO
                                System.out.println("ticket new: ok");
                                break;
                            case "add":
                                if(atributes.length==4) {
                                    if (catalog.getById(Integer.parseInt(atributes[2])) != null) {
                                        ticketActual.add(catalog.getById(Integer.parseInt(atributes[2])), Integer.parseInt(atributes[3]));
                                    }
                                    ticketActual.ticketPrint();
                                    System.out.println("ticket add: ok");
                                    break;
                                }else System.out.println("Comando ticket desconocido.");break;
                            case "remove":
                                this.ticketActual.ticketRemove(Integer.parseInt(atributes[2]));
                                System.out.println("ticket remove: ok");
                                break;
                            case "print":
                                ticketActual.ticketPrint();
                                System.out.println("ticket print: ok");
                                break;
                            default:
                                System.out.println("Comando ticket desconocido.");
                        }
                        break;
                    }else System.out.println("Comando ticket desconocido.");break;
                case "help":
                    this.help();
                    break;
                case "echo":
                    this.echo(mainCommand.substring(5));
                    break;
                case "exit":
                    System.out.println("Closing application.\n" +
                            "Goodbye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Comando desconocido.");
            }
        }
        catch (Exception e){
            System.out.println("Comando no reconocido.");
        }
        return exit;
    }

    private void help() {
        System.out.println("Commands:");
        for (CommandNames command : CommandNames.values()) {
            System.out.println(" " + command.getHelp());
        }

        System.out.println("Categories: MERCH, STATIONERY, CLOTHES, BOOK, ELECTRONICS");
        System.out.println("Discounts if there are ≥2 units in the category: MERCH 0%, STATIONERY 5%, CLOTHES 7%, BOOK 10%, ELECTRONICS 3%.");
    }

    public void echo(String text) {
        if (text == null) {
            System.out.println("Uso: echo \"<texto>\"");
            return;
        }
        System.out.println("echo "+text);
        System.out.flush();
    }


    public static String[] splitCommand(String command) {
        List<String> parts = new ArrayList<>();
        Matcher m = Pattern.compile("\"([^\"]*)\"|(\\S+)").matcher(command);
        while (m.find()) parts.add(m.group(1) != null ? m.group(1) : m.group(2));
        return parts.toArray(new String[0]);
    }




}

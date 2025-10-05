package main.java.upm;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Tienda {

    private Map<String, Product> mapaProductos;
    private int cantidadProductos;

    public static void main(String[] args) {
        Tienda st1 = new Tienda();
    }

    public Tienda() {
        this.mapaProductos = new HashMap<>();
        this.cantidadProductos = 0;
    }

    private boolean ejecutarComando(Scanner scanner) {
        String mainCommand = scanner.next();
        boolean exit = false;

        switch (mainCommand) {
            case "prod":
                String prodCommand = scanner.next();
                switch (prodCommand) {
                    case "add":
                        this.prodAdd(scanner);
                        break;
                    case "list":
                        this.prodList();
                        break;
                    case "update":
                        this.prodUpdate(scanner);
                        break;
                    case "remove":
                        this.prodRemove(scanner);
                        break;
                    default:
                        System.out.println("Comando prod desconocido.");
                }
                break;
            case "ticket":
                String ticketCommand = scanner.next();
                switch (ticketCommand) {
                    case "new":
                        this.ticketNew();
                        break;
                    case "add":
                        this.ticketAdd(scanner);
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





}

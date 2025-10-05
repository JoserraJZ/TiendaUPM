package main.java.upm;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Tienda {

    private Map<String, Producto> mapaProductos;
    private int cantidadProductos;

    public static void main(String[] args) {
        Tienda st1 = new Tienda();
    }

    public Tienda() {
        this.mapaProductos = new HashMap<>();
        this.cantidadProductos = 0;
    }

    private boolean ejecutarComando(Scanner scanner) {
        nombresComandos command = nombresComandos.fromValue(scanner.next());
        boolean exit = false;
        switch (command) {
            case CREATE_USER:
                this.createUser(scanner);
                break;
            case LIST_USERS:
                this.findAllUsers();
                break;
            case HELP:
                this.help();
                break;
            case EXIT:
                exit = true;
                break;
        }
        return exit;
    }





}

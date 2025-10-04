package main.java.upm;

import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private Map<String, Producto> mapaProductos;
    private int cantidadProductos;

    public static void main(String[] args) {
        SistemaTickets st1= new SistemaTickets();
    }
    public Ticket(){
        this.mapaProductos= new HashMap<>();
        this.cantidadProductos=0;
    }
}

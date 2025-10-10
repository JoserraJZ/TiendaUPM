package upm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductCatalog {
    private final HashMap<Integer, Product> products = new HashMap<>();

    public void add(Product product) {
        products.put(product.getIdProducto(), product);
    }

    public boolean update(int id, String campo, Object valor) {
        Product prod = products.get(id);
        if (prod == null) return false;
        switch (campo) {
            case "nombre":
                prod.setNombreProducto((String) valor);
                break;
            case "categoria":
                prod.setCat((Category) valor);
                break;
            case "precio":
                prod.setPrecio((Double) valor);
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean remove(int id) {
        return products.remove(id) != null;
    }

    public void list() {
        for (Product product : products.values()) {
            System.out.println("ID: " + product.getIdProducto() +
                    ", Nombre: " + product.getNombreProducto() +
                    ", Categoria: " + product.getCat() +
                    ", Precio: " + product.getPrecio());
        }
    }

    public Product getById(int id) {
        return products.get(id);
    }

}
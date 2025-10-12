package upm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductCatalog {
    private final HashMap<Integer, Product> products = new HashMap<>();

    public Product add(Product product) {
        products.put(product.getIdProducto(), product);
        return product;
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
        System.out.println("Catalog:");
        for (Product product : products.values()) {
            System.out.printf(" {class:%s, id:%d, name:'%s', category:%s, price:%.1f}%n",
                    "Product", product.getIdProducto(), product.getNombreProducto(), product.getCat().toString(), product.getPrecio());

        }
        System.out.println("prod list: ok");
    }

    public Product getById(int id) {
        return products.get(id);
    }

}
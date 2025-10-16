package upm;

import java.util.HashMap;
import java.util.Locale;

public class ProductCatalog {
    private final HashMap<Integer, Product> products = new HashMap<>();

    public Product add(Product product) {

        if (products.size()<200) {
            products.put(product.getIdProducto(), product);
            return product;
        }
        return null;
    }

    public boolean update(int id, String campo, String valor) {
        Product prod = products.get(id);
        if (prod == null) return false;
        switch (campo) {
            case "nombre":
                prod.setNombreProducto((String) valor);
                break;
            case "categoria":
                prod.setCat(Category.valueOf(valor));
                break;
            case "precio":
                prod.setPrecio(Integer.parseInt(valor));
                break;
            default:
                return false;
        }

        System.out.printf(Locale.US,"{class:%s, id:%d, name:'%s', category:%s, price:%.1f}%n",
                "Product", prod.getIdProducto(), prod.getNombreProducto(), prod.getCat().toString(), prod.getPrecio());

        return true;
    }

    public Product remove(int id) {
        Product prod = products.get(id);
        products.remove(id);
        return prod;
    }

    public void list() {
        System.out.println("Catalog:");
        for (Product product : products.values()) {
            System.out.printf(Locale.US," {class:%s, id:%d, name:'%s', category:%s, price:%.1f}%n",
                    "Product", product.getIdProducto(), product.getNombreProducto(), product.getCat().toString(), product.getPrecio());

        }
        System.out.println("prod list: ok");
    }

    public Product getById(int id) {
        return products.get(id);
    }

}
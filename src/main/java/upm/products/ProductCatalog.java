package upm.products;

import java.util.*;

public class ProductCatalog {
    private final HashMap<Integer, Product> products = new HashMap<>();

    public Product add(Product product) {
        if (products.values().size()<200){
            products.put(product.getId(), product);
            return product;
        }else{
            return null;
        }
    }

    public boolean update(int id, String campo, String valor) {
        Product prod = products.get(id);
        if (prod == null) return false;
        switch (campo) {
            case "NAME":
                prod.setName(valor);
                break;
            case "CATEGORY":
                prod.setCategory(ProductCategory.valueOf(valor));
                break;
            case "PRICE":
                prod.setPrice(Integer.parseInt(valor));
                break;
            default:
                return false;
        }
        System.out.println(prod);
        return true;
    }

    public Product remove(int id) {
        Product prod = products.get(id);
        products.remove(id);
        return prod;
    }

    public void list() {
        System.out.println("Catalog:");

        List<Product> list = new ArrayList<>(products.values());
        list.sort(Comparator.comparing(Product::getId));

        for (Product prod : list) {
            System.out.println(prod);
        }
    }

    public Product getById(int id) {
        return products.get(id);
    }

    public boolean doesIdExist(int id){
        return products.containsKey(id);
    }
}
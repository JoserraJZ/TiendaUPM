package upm.products;

import jakarta.persistence.*;
import upm.RandomGenerator;

import java.util.Locale;

@Entity
@Table(name = "productsCatalog")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;

    private String name;
    private ProductCategory category;
    private double price;

    public Product(String stringId, String productName, ProductCategory category, double price){
        this.id = (stringId == null) ? RandomGenerator.generateProductId() : Integer.parseInt(stringId);
        this.name = productName;
        this.category = category;
        this.price = price;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public ProductCategory getCategory() {
        return category;
    }
    public double getPrice() {
        return price;
    }

    public void setName(String newName) {
        this.name = newName;
    }
    public void setCategory(ProductCategory newCat) {
        this.category = newCat;
    }
    public void setPrice(int newPrice) {
        this.price = newPrice;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Product other = (Product) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "{class:%s, id:%d, name:'%s', category:%s, price:%.1f}",
                "Product", id, name, category, price);
    }

    @Override
    public Product clone() {
        Product copy = new Product(
                String.valueOf(getId()),
                getName(),
                getCategory(),
                getPrice()
        );
        return copy;
    }

}

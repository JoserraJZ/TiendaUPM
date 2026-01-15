package upm.ticketitems;


import jakarta.persistence.*;
import upm.products.Product;
import upm.products.ProductCategory;

//@Entity
public class ProductItem implements TicketItem{

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;


    //@ManyToOne
    private final Product prod;
    private int quantity = -1;

    public ProductItem(Product prod, int quantity) {
        this.prod = prod;
        this.quantity = quantity;
    }

    public Product getProd() {
        return prod;
    }

    @Override
    public String getClassStr() { return prod.getClass().getSimpleName(); }

    @Override
    public String getItemId() {return Integer.toString(prod.getId());}

    @Override
    public void addQuantity(int toAdd){ quantity += toAdd;}
    @Override
    public int getQuantity() {return quantity;}

    @Override
    public String toString() { return String.format(prod.toParametersString(), getPrice(), ", actual people in event:"+quantity); }

    @Override
    public ProductCategory getCategory() { return prod.getCategory(); }

    @Override
    public double getPrice() {
        return prod.getPrice() * quantity;
    }
}
package upm.ticketitems;


import upm.products.Product;
import upm.products.ProductCategory;

public class ProductItem implements TicketItem{


    private final Product prod;
    private int quantity = -1;

    public ProductItem(Product prod, int quantity) {
        this.prod = prod;
        this.quantity = quantity;
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
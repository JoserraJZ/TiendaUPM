package upm.ticketitems;

import jakarta.persistence.Entity;
import upm.products.ProductCategory;


public interface TicketItem {
    String getClassStr();
    void addQuantity(int toAdd);
    int getQuantity();
    String getItemId();
    String toString();

    ProductCategory getCategory();

    double getPrice();
}

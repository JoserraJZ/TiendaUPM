package upm.ticketitems;

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

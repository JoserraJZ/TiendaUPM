package upm.ticketitems;

import jakarta.persistence.*;
import upm.Service;
import upm.products.ProductCategory;

import java.util.Locale;

//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class ServiceItem implements TicketItem{

    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;


    //@ManyToOne
    private final Service service;

    public ServiceItem(Service service) {
        this.service = service;
    }


    @Override
    public String getClassStr() { return "Service"; }
    @Override
    public String getItemId() {return Integer.toString(service.getId())+"s";}

    @Override
    public void addQuantity(int toAdd) {}
    @Override
    public int getQuantity() {return 1;}

    @Override
    public String toString() { return service.toString(); }

    @Override
    public ProductCategory getCategory() { return null;}

    @Override
    public double getPrice() {
        return 0.15;
    }
}

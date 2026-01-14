package upm;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import upm.products.Product;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ProductAdded")
public class ProductAdded {

    @Id
    private String idTicket;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int cuantity;

    private List<String> personalizedTexts = new ArrayList<>();


    protected ProductAdded() {
        // Constructor requerido por Hibernate
    }

    public ProductAdded(String idTicket, Product product, int cuantity) {
        this.idTicket = idTicket;
        this.product = product;
        this.cuantity=cuantity;
    }

    public void addText(String text){
        personalizedTexts.add(text);
    }

    public String getIdTicket() {
        return idTicket;
    }

    public Product getProduct() {
        return product;
    }
}
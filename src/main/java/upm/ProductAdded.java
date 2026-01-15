package upm;

import jakarta.persistence.*;
import upm.products.Product;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ProductAdded")
public class ProductAdded {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // ID autogenerado

    private String idTicket;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int cuantity;

    private List<String> personalizedTexts = new ArrayList<>();


    protected ProductAdded() {
        // Constructor requerido por Hibernate
    }

    public ProductAdded(String idTicket, Product product, int cuantity, List<String> personalizedTexts) {
        this.idTicket = idTicket;
        this.product = product;
        this.cuantity=cuantity;
        this.personalizedTexts = personalizedTexts;
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
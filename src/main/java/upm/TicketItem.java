package upm;

import jakarta.persistence.*;

import upm.products.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ticket_items")
public class TicketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID técnico, no de negocio

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    @ElementCollection
    @CollectionTable(
            name = "ticket_item_texts",
            joinColumns = @JoinColumn(name = "ticket_item_id")
    )
    @Column(name = "text")
    private List<String> personalizedTexts = new ArrayList<>();

    public TicketItem() {}

    public TicketItem(Product p, int cuantity, Ticket ticket){
        personalizedTexts=new ArrayList<>();
        this.product=p;
        this.quantity = quantity;
        this.ticket=ticket;
    }

    public String getTicketId() {
        return ticket.getId();
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
    public ProductCategory getCategory() {
        return product.getCategory();
    }

    public void addPersonalizedText(String text) {
        if (personalizedTexts==null){
            personalizedTexts= new ArrayList<>();
        }
        if(product instanceof CustomizableProduct cp) {
            if (personalizedTexts.size() >= cp.getMaxTexts()) {
                System.out.println("No se pueden añadir más textos personalizados.");
            }
            personalizedTexts.add(text);
        }
    }

    public boolean compareCustomizableProducts(TicketItem other) {
        if (this.personalizedTexts.size() != other.personalizedTexts.size()) {
            return false;
        }
        for (int i = 0; i < this.personalizedTexts.size(); i++) {
            if (!this.personalizedTexts.get(i).equals(other.personalizedTexts.get(i))) {
                return false;
            }
        }
        return true;
    }



    public double getPriceCustomizable() {

        double basePrice = product.getPrice();
        double surcharge = basePrice * 0.10 * personalizedTexts.size();
        return basePrice + surcharge;
    }

    public void updateCuantity(int amount) {
        this.quantity+=amount;
    }

    public double getPrice() {
       return product.getPrice()*quantity;
    }

    public List<String> getPersonalizedTexts() {
        return personalizedTexts;
    }

    @Override
    public String toString() {
        if (product instanceof  ProductMeeting pM){
            return pM.toString(quantity);
        } else if (product instanceof ProductCampusFood pCF) {

            return pCF.toString(quantity);

        }
        return "";
    }
}

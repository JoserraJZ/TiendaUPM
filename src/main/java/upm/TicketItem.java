package upm;

import jakarta.persistence.*;

import upm.RandomGenerator;
import upm.products.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "ticketItems")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)

public class TicketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final String ticketId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int cuantity;

    private List<String> personalizedTexts;


    public TicketItem(Product p, int cuantity, String ticketId){
        personalizedTexts=null;
        this.product=p;
        this.cuantity = cuantity;
        this.ticketId=ticketId;
    }

    public String getId() {
        return ticketId;
    }

    public Product getProduct() {
        return product;
    }

    public int getCuantity() {
        return cuantity;
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
        this.cuantity+=amount;
    }

    public double getPrice() {
       return product.getPrice()*cuantity;
    }

    @Override
    public String toString() {
        if (product instanceof  ProductMeeting pM){
            return pM.toString(cuantity);
        } else if (product instanceof ProductCampusFood pCF) {

            return pCF.toString(cuantity);

        }
        return "";
    }
}

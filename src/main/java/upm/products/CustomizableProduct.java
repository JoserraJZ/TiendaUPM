package upm.products;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Entity
@Table(name = "CustomizableProduct")
public class CustomizableProduct extends Product {

    private int maxTexts;
    List<String> personalizedTexts = new ArrayList<>();

    public CustomizableProduct(String stringId, String productName, ProductCategory category, double basePrice, int maxTexts) {
        super(stringId, productName, category, basePrice);
        this.maxTexts = maxTexts;
    }
    protected CustomizableProduct() {
    }

    public void addText(String text){
        personalizedTexts.add(text);
    }

    public int getMaxTexts() {
        return maxTexts;
    }

    public List<String> getPersonalizedTexts() {
        return personalizedTexts;
    }

    @Override
    public double getPrice() {
        return super.getPrice() + 0.1 * super.getPrice() * personalizedTexts.size();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public CustomizableProduct clone() {
        return new CustomizableProduct(
                String.valueOf(super.getId()),
                super.getName(),
                super.getCategory(),
                super.getPrice(),
                this.maxTexts
        );
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null) return false;
        if (getClass() != other.getClass()) return false;

        CustomizableProduct that = (CustomizableProduct) other;

        // Compara primero los campos heredados usando el equals del padre
        if (!super.equals(that)) return false;

        // Compara maxTexts
        if (this.maxTexts != that.maxTexts) return false;

        // Compara la lista de textos personalizados
        return true;
    }


    public String toString() {
        if (personalizedTexts.isEmpty()){
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', category:%s, price:%.1f, maxPersonal:%d}",
                    "ProductPersonalized", super.getId(), super.getName(), super.getCategory(), getPrice(), getMaxTexts());
        } else {
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', category:%s, price:%.1f, maxPersonal:%d, personalizationList:%s}",
                    "ProductPersonalized", super.getId(), super.getName(), super.getCategory(), getPrice(), getMaxTexts(), personalizedTexts);
        }
    }
}

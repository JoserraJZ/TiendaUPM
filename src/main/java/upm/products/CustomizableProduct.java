package upm.products;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@Entity
@DiscriminatorValue("CustomizableProduct")
public class CustomizableProduct extends Product {

    private final int maxTexts;

    public CustomizableProduct(String stringId, String productName, ProductCategory category, double basePrice, int maxTexts) {
        super(stringId, productName, category, basePrice);
        this.maxTexts = maxTexts;
    }



    public int getMaxTexts() {
        return maxTexts;
    }

    @Override
    public double getPrice() {
        return super.getPrice();
    }

    @SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
    public CustomizableProduct clone() {
        CustomizableProduct copy = new CustomizableProduct(
                String.valueOf(super.getId()),
                super.getName(),
                super.getCategory(),
                super.getPrice(),
                this.maxTexts
        );
        return copy;
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


    public String toString(List<String> personalizedTexts, double precioCalculado) {
        if (!personalizedTexts.isEmpty()){
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', category:%s, price:%.1f, maxPersonal:%d, personalizationList:%s}",
                    "ProductPersonalized", super.getId(), super.getName(), super.getCategory(), precioCalculado, getMaxTexts(), personalizedTexts);
        }else {
            return String.format(Locale.US,
                "{class:%s, id:%d, name:'%s', category:%s, price:%.1f, maxPersonal:%d}",
                "ProductPersonalized", super.getId(), super.getName(), super.getCategory(), getPrice(), getMaxTexts());
        }
    }


}

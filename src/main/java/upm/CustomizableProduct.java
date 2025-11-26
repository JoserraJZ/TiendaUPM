package upm;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomizableProduct extends Product {

    private final int maxTexts;
    private final List<String> personalizedTexts;

    public CustomizableProduct(String stringId, String productName, ProductCategory category, double basePrice, int maxTexts) {
        super(stringId, productName, category, basePrice);
        this.maxTexts = maxTexts;
        this.personalizedTexts = new ArrayList<>();
    }

    public boolean addPersonalizedText(String text) {
        if (personalizedTexts.size() >= maxTexts) {
            return false; // No se puede añadir más textos
        }
        personalizedTexts.add(text);
        return true;
    }

    public int getMaxTexts() {
        return maxTexts;
    }

    @Override
    public double getPrice() {
        double basePrice = super.getPrice();
        double surcharge = basePrice * 0.10 * personalizedTexts.size();
        return basePrice + surcharge;
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

        for (String text : this.personalizedTexts) {
            copy.addPersonalizedText(text);
        }

        return copy;
    }


    @Override
    public String toString() {
        if (!personalizedTexts.isEmpty()){
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', category:%s, price:%.1f, maxPersonal:%d, personalizationList:%s}",
                    "ProductPersonalized", super.getId(), super.getName(), super.getCategory(), getPrice(), getMaxTexts(), personalizedTexts);
        }else {
            return String.format(Locale.US,
                "{class:%s, id:%d, name:'%s', category:%s, price:%.1f, maxPersonal:%d}",
                "ProductPersonalized", super.getId(), super.getName(), super.getCategory(), getPrice(), getMaxTexts());
        }
    }


}

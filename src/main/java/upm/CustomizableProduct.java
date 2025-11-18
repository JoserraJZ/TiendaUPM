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

        /// ////////VALIDAR texto maximo positivo en el bucle de tienda
    }

    public boolean addPersonalizedText(String text) {
        if (personalizedTexts.size() >= maxTexts) {
            return false; // No se puede añadir más textos
        }
        personalizedTexts.add(text);
        return true;
    }

    public List<String> getPersonalizedTexts() {
        return new ArrayList<>(personalizedTexts); // copia defensiva
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

    @Override
    public String toString() {
        if (maxTexts>0){
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', category:%s, price:%.1f, maxPersonal:%d, personalizationList:'%s'}",
                    "Product", super.getId(), super.getName(), super.getCategory(), getPrice(), getMaxTexts(), personalizedTexts);
        }else {
            return String.format(Locale.US,
                "{class:%s, id:%d, name:'%s', category:%s, price:%.1f, maxPersonal:%d }",
                "Product", super.getId(), super.getName(), super.getCategory(), getPrice(), getMaxTexts());
        }
    }

}

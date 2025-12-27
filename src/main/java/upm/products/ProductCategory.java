package upm.products;

public enum ProductCategory {
    MERCH(0),
    STATIONERY(5),
    CLOTHES(7),
    BOOK(10),
    ELECTRONICS(3);

    private final int discountPercent;
    ProductCategory(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }
}
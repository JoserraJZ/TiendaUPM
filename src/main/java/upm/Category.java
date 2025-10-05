package main.java.upm;

public enum Category {
    MERCH(0), STATIONERY(5), CLOTHES(7), BOOK(10), ELECTRONICS(3);

    private final int discountPercent;

    Category(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }
}
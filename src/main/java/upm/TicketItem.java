package main.java.upm;

public class TicketItem {
    private final Product product;
    private final int quantity;

    public TicketItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Calcula el descuento total para este ítem
    public double calcularDescuento() {
        int descuento = product.getCat().getDiscountPercent();
        double subtotal = product.getPrecio() * quantity;
        return subtotal * descuento / 100.0;
    }

    // Imprime la línea del ticket
    public void imprimirLinea() {
        double subtotal = product.getPrecio() * quantity;
        double descuento = calcularDescuento();
        System.out.printf("%d x %s (%.2f€) = %.2f€  Descuento: -%.2f€\n",
                quantity,
                product.getNombreProducto(),
                product.getPrecio(),
                subtotal,
                descuento
        );
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}

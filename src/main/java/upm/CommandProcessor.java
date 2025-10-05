package main.java.upm;

public class CommandProcessor {
    private final ProductCatalog catalog;
    private final Ticket ticket;

    public CommandProcessor(ProductCatalog catalog, Ticket ticket) {
        this.catalog = catalog;
        this.ticket = ticket;
    }

    // Procesa un comando de texto
    public void process(String input) {
        String[] parts = input.trim().split("\\s+");
        if (parts.length == 0) return;

        switch (parts[0].toLowerCase()) {
            case "listar":
                for (Product p : catalog.list()) {
                    System.out.printf("%d: %s (%.2f€) [%s]\n", p.getIdProducto(), p.getNombreProducto(), p.getPrecio(), p.getCat());
                }
                break;
            case "agregar":
                if (parts.length < 3) {
                    System.out.println("Uso: agregar <id> <cantidad>");
                    break;
                }
                try {
                    int id = Integer.parseInt(parts[1]);
                    int cantidad = Integer.parseInt(parts[2]);
                    Product prod = catalog.getById(id);
                    if (prod == null) {
                        System.out.println("Producto no encontrado.");
                    } else {
                        // Suponiendo que Ticket tiene un método público add(Product, int)
                        ticket.add(prod, cantidad);
                        System.out.println("Producto agregado al ticket.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("ID o cantidad inválidos.");
                }
                break;
            default:
                System.out.println("Comando no reconocido.");
        }
    }
}

package upm;

public class CommandProcessor {
    private final ProductCatalog catalog;
    private final Ticket ticket;

    public CommandProcessor(ProductCatalog catalog, Ticket ticket) {
        this.catalog = catalog;
        this.ticket = ticket;
    }

    // Procesa un comando de texto
    public void process(String input) {

    }
}

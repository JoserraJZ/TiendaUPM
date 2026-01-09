package upm;

import jakarta.persistence.*;
import java.util.Locale;

@Entity
@Table(name = "serviceItems")
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final String ticketId;

    @ManyToOne
    private Service service;

    private int quantity;

    public ServiceItem(Service service, int quantity, String ticketId) {
        this.service = service;
        this.quantity = quantity;
        this.ticketId = ticketId;
    }

    public String getId() {
        return ticketId;
    }

    public Service getService() {
        return service;
    }

    public int getQuantity() {
        return quantity;
    }

    public void updateQuantity(int amount) {
        this.quantity += amount;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "{class:%s, id:%s, serviceId:%d, category:'%s', quantity:%d}",
                "ServiceItem",
                ticketId,
                service.getId(),
                service.getCategory(),
                quantity
        );
    }
}

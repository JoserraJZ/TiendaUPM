package upm;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "AssignedTickets")
public class AssignedTickets {

    @Id
    @Column(name = "ticket_id", nullable = false)
    private String ticketId;

    @Column(name = "cashier_id", nullable = false)
    private String cashierId;

    protected AssignedTickets() {}

    public AssignedTickets(String ticketId, String cashierId) {
        this.ticketId = ticketId;
        this.cashierId = cashierId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashierId(String cashierId) {
        this.cashierId = cashierId;
    }
}
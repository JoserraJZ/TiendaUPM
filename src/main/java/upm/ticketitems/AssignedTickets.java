package upm.ticketitems;

import jakarta.persistence.*;

@Entity
@Table(name = "assigned_tickets")
public class AssignedTickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // ID autogenerado por la BD

    // PK artificial para la tabla

    private String idCajero;
    private String idTicket;

    public AssignedTickets() {}

    public AssignedTickets(String idCajero, String idTicket) {
        this.idCajero = idCajero;
        this.idTicket = idTicket;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(String idCajero) {
        this.idCajero = idCajero;
    }

    public String getIdTicket() {
        return idTicket;
    }

    public void setIdTicket(String idTicket) {
        this.idTicket = idTicket;
    }
}

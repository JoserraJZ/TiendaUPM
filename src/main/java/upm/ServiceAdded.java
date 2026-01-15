package upm;

import jakarta.persistence.*;
import upm.Service;

@Entity
@Table(name = "ServiceAdded")
public class ServiceAdded {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // ID autogenerado

    private String idTicket;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    protected ServiceAdded() {
        // Constructor requerido por Hibernate
    }

    public ServiceAdded(String idTicket, Service service) {
        this.idTicket = idTicket;
        this.service = service;
    }

    public String getTicketId() {
        return idTicket;
    }

    public Service getService() {
        return service;
    }
}

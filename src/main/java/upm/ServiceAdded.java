package upm;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import upm.Service;

@Entity
@Table(name = "ServiceAdded")
public class ServiceAdded {

    @Id
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

    public String getIdTicket() {
        return idTicket;
    }

    public Service getService() {
        return service;
    }
}

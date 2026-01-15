package upm;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ClientCompany")

public class ClientCompany extends Client{

    public ClientCompany(String name, String NIF, String email, Cashier cashier, ClientType type) {
        super(name, NIF, email, cashier, type);
    }
    protected ClientCompany() {
        // Constructor requerido por Hibernate
    }

    //Comprobar NIF antes de llegar aquí
    //Ver como aceptar Servicios o Productos+Servicios
    //No mostrar precio de Servicios
    //Identificar por NIF

    @Override
    public String toString() {
        return super.toString().replaceFirst("USER", "COMPANY");
    }


}

package upm;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ClientCompany")

public class ClientCompany extends Client{

    private final ClientType type = ClientType.COMPANY;
    public ClientType getType() {return type;}

    public ClientCompany(String name, String NIF, String email, Cashier cashier) {
        super(name, NIF, email, cashier);
    }
    protected ClientCompany() {
        // Constructor requerido por Hibernate
    }

    @Override
    public String toString() {
        return super.toString().replaceFirst("USER", "COMPANY");
    }

}

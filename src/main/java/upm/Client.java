package upm;

import jakarta.persistence.*;


import java.util.Objects;

@Entity
@Table(name = "client")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public class Client {

    @Id
    private String id;

    private String name;
    private String email;

    @Transient
    private Cashier cashier;

    private String cashierId;

    private ClientType clientType;

    public Client(String name, String DNI, String email, Cashier cashier, ClientType type){
        this.name=name;
        this.id = (DNI == null) ? RandomGenerator.generateDNI() : DNI;
        this.email=email;
        this.cashier=cashier;
        this.clientType=type;
        this.cashierId= cashier.getId();

        System.out.println(this);
    }

    protected Client(){
    }
    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format(
                "USER{identifier='%s', name='%s', email='%s', cash=%s}",
                id, name, email, cashier.getId()
        );
    }

    public String getCashierId() {
        return cashierId;
    }

    public void setCashier(Cashier cashier) {
        this.cashier = cashier;
    }

    public Client clone() {
        Client copy = new Client(
                this.name,
                this.id,          // mismo DNI/id
                this.email,
                this.cashier,     // mismo cashier en memoria (transient)
                this.clientType
        );
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Client) o).id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

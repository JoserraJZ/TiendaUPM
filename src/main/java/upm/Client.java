package main.java.upm;

import java.util.Objects;

public class Client {
    private String name;
    private String id;
    private String email;
    private Cashier cashier;

    public Client(String name, String DNI, String email, Cashier cashier){
        this.name=name;
        this.id = (DNI == null) ? RandomGenerator.generateDNI() : DNI;
        this.email=email;
        this.cashier=cashier;

        System.out.println(this);
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
                "Client{identifier='%s', name='%s', email='%s', cash=%s}",
                id, name, email, cashier.getId()
        );
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

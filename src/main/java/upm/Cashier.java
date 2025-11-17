package upm;

import java.util.Objects;

public class Cashier {

    private String id;
    private String name;
    private String businessEmail;

    public Cashier(String id, String newName, String newBusinessEmail){
        this.id = (id == null) ? RandomGenerator.generateCashierId() : id;

        this.name=newName;
        this.businessEmail= newBusinessEmail;

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
                "Cash{identifier='%s', name='%s', email='%s'}", id, name, businessEmail
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(id, ((Cashier) o).id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

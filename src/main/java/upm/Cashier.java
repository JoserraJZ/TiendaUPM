package upm;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

@Entity
@Table(name = "cashier")
public class Cashier implements Comparable<Cashier>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final String id;

    private final String name;
    private final String businessEmail;

    @OneToMany
    private final Set<Ticket> tickets = new HashSet<>();

    public Cashier(String id, String newName, String newBusinessEmail){
        this.id = (id == null) ? RandomGenerator.generateCashierId() : id;

        this.name = newName;
        this.businessEmail = newBusinessEmail;

        System.out.println(this);
    }

    public String getName() {
        return name;
    }
    public String getId() {
        return id;
    }

    public Set<Ticket> getTickets() {
        return Collections.unmodifiableSet(tickets);
    }

    public boolean addTicket(Ticket ticket) {
        if (ticket == null) return false;
        return tickets.add(ticket);
    }

    @Override
    public String toString() {
        return String.format(
                "Cash{identifier='%s', name='%s', email='%s'}", id, name, businessEmail
        );
    }

    @Override
    public int compareTo(Cashier o) {
        return this.id.compareTo(o.id);
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
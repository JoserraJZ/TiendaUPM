package upm;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public class Cashier {

    private String id;
    private String name;
    private String businessEmail;
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

    // Devuelve la colección de tickets como un Set inmodificable
    public Set<Ticket> getTickets() {
        return Collections.unmodifiableSet(tickets);
    }

    // Métodos auxiliares para gestionar tickets
    public boolean addTicket(Ticket ticket) {
        if (ticket == null) return false;
        return tickets.add(ticket);
    }

    public boolean removeTicket(Ticket ticket) {
        if (ticket == null) return false;
        return tickets.remove(ticket);
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
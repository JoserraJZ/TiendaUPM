package upm;


import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "cashier")
public class Cashier implements Comparable<Cashier>{

    @Id
    private String id;

    private String name;
    private String businessEmail;

    @Transient
    private final Set<Ticket> tickets = new HashSet<>();



    public Cashier(String id, String newName, String newBusinessEmail){
        this.id = (id == null) ? RandomGenerator.generateCashierId() : id;

        this.name = newName;
        this.businessEmail = newBusinessEmail;

        System.out.println(this);
    }

    protected Cashier(){
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

    public void listTicket() {
        if (this != null) {
            List<Ticket> tickets = new ArrayList<>(this.getTickets());
            tickets.sort(Comparator.comparing(Ticket::getId));

            System.out.println("Tickets: ");
            tickets.forEach(t -> System.out.println(t.getId() + " ->" + t.getCurrentState()));
        }
        else Tienda.printError("El identificador de cajero introducido no existe");
    }

    @Override
    public String toString() {
        return String.format(
                "Cash{identifier='%s', name='%s', email='%s'}", id, name, businessEmail
        );
    }

    @Override
    public Cashier clone() {
        return new Cashier(
                this.id,              // mismo id
                this.name,            // mismo nombre
                this.businessEmail    // mismo email
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
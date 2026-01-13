package upm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import upm.products.Product;
import upm.ticketitems.AssignedTickets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class HibernateUtils {

    private SessionFactory factory = null;

    public HibernateUtils () {

        try {
            // Intento normal: si la BD existe, Hibernate la abre
            factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Client.class)
                    .addAnnotatedClass(Cashier.class)
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(Service.class)
                    .addAnnotatedClass(Ticket.class)
                    .buildSessionFactory();

            System.out.println("Base de datos encontrada y cargada correctamente.");

        } catch (Exception e) {
            System.out.println("No se encontró la base de datos. Creando una nueva...");

            try {
                // Segundo intento: forzar creación del esquema
                factory = new Configuration()
                        .configure("hibernate_create.cfg.xml") // archivo alternativo
                        .addAnnotatedClass(Client.class)
                        .addAnnotatedClass(Cashier.class)
                        .addAnnotatedClass(Product.class)
                        .addAnnotatedClass(Service.class)
                        .addAnnotatedClass(Ticket.class)
                        .buildSessionFactory();

                System.out.println("Base de datos creada correctamente.");

            } catch (Exception ex) {
                System.out.println("Error crítico al crear la base de datos.");
                ex.printStackTrace();
            }
        }


    }

    public void cargarClientes(Set<Client> cliente) {

        Session session = null;
        Transaction tx = null;
        List<Client> clients = new ArrayList<>();

        try {
            session = factory.openSession();   // Abrir sesión
            tx = session.beginTransaction();          // Iniciar transacción

            clients = session.createQuery("FROM Ticket", Client.class).getResultList();
            tx.commit();                              // Confirmar transacción
        } catch (Exception e) {
            if (tx != null) tx.rollback();            // Revertir si hay error
            throw e;
        } finally {
            if (session != null) session.close();     // Cerrar sesión
        }

        cliente.addAll(clients);
    }

    public void loadClients(Set<Client> client) {

        Session session = null;
        Transaction tx = null;
        List<Client> clients = new ArrayList<>();

        try {
            session = factory.openSession();   // Abrir sesión
            tx = session.beginTransaction();          // Iniciar transacción

            clients = session.createQuery("FROM client", Client.class).getResultList();
            tx.commit();                              // Confirmar transacción
        } catch (Exception e) {
            if (tx != null) tx.rollback();            // Revertir si hay error
            throw e;
        } finally {
            if (session != null) session.close();     // Cerrar sesión
        }

        client.addAll(clients);
    }

    public void loadCashiers(Set<Cashier> cashier) {

        Session session = null;
        Transaction tx = null;
        List<Cashier> cashiers = new ArrayList<>();

        try {
            session = factory.openSession();   // Abrir sesión
            tx = session.beginTransaction();          // Iniciar transacción

            cashiers = session.createQuery("FROM cashier", Cashier.class).getResultList();
            tx.commit();                              // Confirmar transacción
        } catch (Exception e) {
            if (tx != null) tx.rollback();            // Revertir si hay error
            throw e;
        } finally {
            if (session != null) session.close();     // Cerrar sesión
        }

        cashier.addAll(cashiers);
    }

    public void loadTickets(Set<Cashier> cashier) {

        Session session = null;
        Transaction tx = null;
        List<Ticket> tickets = new ArrayList<>();
        List<AssignedTickets> assignation = new ArrayList<>();

        try {
            session = factory.openSession();   // Abrir sesión
            tx = session.beginTransaction();          // Iniciar transacción

            tickets = session.createQuery("FROM tickets", Ticket.class).getResultList();
            assignation= session.createQuery("FROM assigned_tickets", AssignedTickets.class).getResultList();
            tx.commit();                              // Confirmar transacción
        } catch (Exception e) {
            if (tx != null) tx.rollback();            // Revertir si hay error
            throw e;
        } finally {
            if (session != null) session.close();     // Cerrar sesión
        }

        for (AssignedTickets at: assignation){
            Cashier cashierForAssign = cashier.stream()
                    .filter(c -> c.getId().equals(at.getIdCajero()))
                    .findFirst()
                    .orElse(null);

            Ticket ticketForAssign = tickets.stream()
                    .filter(c -> c.getId().equals(at.getIdTicket()))
                    .findFirst()
                    .orElse(null);
            cashierForAssign.addTicket(ticketForAssign);

        }

    }




    public void addClientToDb(Client c) {
        if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.persist(c); // Reemplaza a save en Hibernate 7
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido añadir a la bd por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }
    }


    public void addCashierToDb(Cashier ca){
        if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.persist(ca); // Reemplaza a save en Hibernate 7
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido añadir el cajero a la bd por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }
    }
    public void addServiceToDb(Service s){
        if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.persist(s); // Reemplaza a save en Hibernate 7
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido añadir el servicio a la bd por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }
    }
    public void addProductToDb(Product p){
        if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.persist(p); // Reemplaza a save en Hibernate 7
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido añadir el producto a la bd por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }
    }

    public void addTicketToDb(Ticket t){
        if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.persist(t); // Reemplaza a save en Hibernate 7
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido el ticket añadir a la bd por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }
    }


    public void endConnection(){
        factory.close();
    }



}


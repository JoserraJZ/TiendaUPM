package upm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import upm.Tables.AssignedTickets;
import upm.Tables.ProductAdded;
import upm.Tables.ServiceAdded;
import upm.products.CustomizableProduct;
import upm.products.Product;
import upm.products.ProductCampusFood;
import upm.products.ProductMeeting;
import upm.ticketitems.ProductItem;
import upm.ticketitems.ServiceItem;
import upm.ticketitems.TicketItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class HibernateUtils {

    private SessionFactory factory = null;

    private static HibernateUtils instance;
    public static HibernateUtils getInstance(){
        if (instance == null){
            instance = new HibernateUtils();
        }

        return instance;
    }

    public HibernateUtils () {
        instance = this;

        try {
            // Intento normal: si la BD existe, Hibernate la abre
            factory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Client.class)
                    .addAnnotatedClass(Cashier.class)
                    .addAnnotatedClass(Product.class)
                    .addAnnotatedClass(Service.class)
                    .addAnnotatedClass(Ticket.class)
                    .addAnnotatedClass(ServiceAdded.class)
                    .addAnnotatedClass(ProductAdded.class)
                    .addAnnotatedClass(CustomizableProduct.class)
                    .addAnnotatedClass(ProductCampusFood.class)
                    .addAnnotatedClass(ProductMeeting.class)
                    .addAnnotatedClass(AssignedTickets.class)
                    .addAnnotatedClass(ClientCompany.class)


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
                        .addAnnotatedClass(ServiceAdded.class)
                        .addAnnotatedClass(ProductAdded.class)
                        .addAnnotatedClass(CustomizableProduct.class)
                        .addAnnotatedClass(ProductCampusFood.class)
                        .addAnnotatedClass(ProductMeeting.class)
                        .addAnnotatedClass(AssignedTickets.class)
                        .addAnnotatedClass(ClientCompany.class)


                        .buildSessionFactory();

                System.out.println("Base de datos creada correctamente.");

            } catch (Exception ex) {
                System.out.println("Error crítico al crear la base de datos.");
                ex.printStackTrace();
            }
        }
    }


    public void endConnection() {
        if (factory != null) {
            factory.close();
        }
    }

    public void emptyTables(){

        if (factory != null) {
            Session session = factory.openSession();
            try {
                session.beginTransaction();
                session.createNativeMutationQuery("DELETE FROM ProductAdded").executeUpdate();
                // session.createNativeMutationQuery("DELETE FROM sqlite_sequence WHERE name = 'ProductAdded'").executeUpdate();

                session.createNativeMutationQuery("DELETE FROM ServiceAdded").executeUpdate();
                // session.createNativeMutationQuery("DELETE FROM sqlite_sequence WHERE name = 'ServiceAdded'").executeUpdate();

                session.createNativeMutationQuery("DELETE FROM AssignedTickets").executeUpdate();
                session.createNativeMutationQuery("DELETE FROM Tickets").executeUpdate();
                session.createNativeMutationQuery("DELETE FROM Client").executeUpdate();
                session.createNativeMutationQuery("DELETE FROM Cashier").executeUpdate();

                session.createNativeMutationQuery("DELETE FROM productCatalog").executeUpdate();
                session.createNativeMutationQuery("DELETE FROM Service").executeUpdate();

                session.getTransaction().commit();
                session.close();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido borrar las tablas por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }
    }

    public void SaveApp(Catalog<Product> productCatalog, Catalog<Service> serviceCatalog,
                        Set<Cashier> cashiers, Set<Client> clients ) {
        if (factory != null) {
            Session session = factory.openSession();
            try {
                session.beginTransaction();
                for (Product p : productCatalog.items.values()) {
                    session.merge(p);
                }
                for (Service s : serviceCatalog.items.values()) {
                    session.merge(s);
                }
                for (Cashier c : cashiers) {
                    for (Ticket t : c.getTickets()) {
                        for (TicketItem ti: t.getItems()) {

                            if (ti instanceof ProductItem pi){
                                if (pi.getProd() instanceof CustomizableProduct){
                                    CustomizableProduct cp= (CustomizableProduct) pi.getProd().clone();
                                    ProductAdded p = new ProductAdded(t.getId(), pi.getProd(), pi.getQuantity(), cp.getPersonalizedTexts());
                                    session.merge(p);
                                }else {
                                    ProductAdded p = new ProductAdded(t.getId(), pi.getProd(), pi.getQuantity(), null);
                                    session.merge(p);
                                }
                            } else if (ti instanceof ServiceItem si){
                                ServiceAdded tiS = new ServiceAdded(t.getId(),si.getService());
                                session.merge(tiS);
                            }

                        }
                        //GuardarLosTickets
                        AssignedTickets as = new AssignedTickets(t.getId(), c.getId());
                        session.merge(as);
                        session.merge(t);
                    }
                    session.merge(c);
                }
                for(Client c : clients){
                    session.merge(c);
                }


                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido guardar la aplicación por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }

    }

    public void loadDb(Catalog<Product> productCatalog, Catalog<Service> serviceCatalog,
                       Set<Cashier> cashiers, Set<Client> clients ){
        if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();

                List<Product> listaProductsFromDB = session
                        .createQuery("FROM Product", Product.class)
                        .getResultList();
                for (Product p: listaProductsFromDB) {
                    if (p instanceof CustomizableProduct){
                        CustomizableProduct cp= (CustomizableProduct) p.clone();
                        productCatalog.add(cp.getId(), cp);
                    }else if(p instanceof ProductCampusFood){
                        ProductCampusFood pcf= (ProductCampusFood) p.clone();
                        productCatalog.add(pcf.getId(), pcf);
                    } else if (p instanceof  ProductMeeting) {
                        ProductMeeting pm= (ProductMeeting) p.clone();
                        productCatalog.add(pm.getId(), pm);
                    }else {
                        Product prod = p.clone();
                        productCatalog.add(prod.getId(), prod);
                    }
                }
                List<Service> listaServicesFromDb = session
                        .createQuery("FROM Service", Service.class)
                        .getResultList();
                for (Service s: listaServicesFromDb) {
                    serviceCatalog.add(s.getId(), s);
                }

                List<Cashier> listaCashiersFromDb = session
                        .createQuery("FROM Cashier", Cashier.class)
                        .getResultList();
                for (Cashier c: listaCashiersFromDb) {
                    cashiers.add(c);
                }
                List<Client> listaClientsFromDb = session
                        .createQuery("FROM Client", Client.class)
                        .getResultList();
                for (Client c: listaClientsFromDb) {
                    if (c instanceof ClientCompany cc){
                        clients.add(cc);
                        continue;
                    }
                    clients.add(c);
                }

                List<Ticket> listaTicketsFromDb = session
                        .createQuery("FROM Ticket", Ticket.class)
                        .getResultList();
                List<Ticket> ticketCargados= new ArrayList<>();
                for(Ticket t: listaTicketsFromDb){
                    ticketCargados.add(t);
                }

                List<ServiceAdded> listaAddedServicesFromDB= session
                        .createQuery("FROM ServiceAdded", ServiceAdded.class)
                        .getResultList();

                for(ServiceAdded sa: listaAddedServicesFromDB){
                    Service serv= serviceCatalog.getById(sa.getService().getId()).clone();
                    Ticket assignationTicket= ticketCargados.stream()
                            .filter(tt -> tt.getId().equals(sa.getTicketId()))
                            .findFirst()
                            .orElse(null);
                    TicketItem ti = new ServiceItem(serv);
                    assignationTicket.getItems().add(ti);
                }

                List<ProductAdded> listaProductsAddedFromDb= session
                        .createQuery("FROM ProductAdded", ProductAdded.class)
                        .getResultList();
                for(ProductAdded pa: listaProductsAddedFromDb){
                    Product prod= productCatalog.getById(pa.getProduct().getId()).clone();
                    if (pa.getPersonalizedTexts()!=null){
                        if (!pa.getPersonalizedTexts().isEmpty()){
                            CustomizableProduct cp= (CustomizableProduct) prod;
                            for (String text: pa.getPersonalizedTexts()) {
                                cp.addText(text);
                            }
                        }
                    }
                    Ticket assignationTicket= ticketCargados.stream()
                            .filter(tt -> tt.getId().equals(pa.getTicketId()))
                            .findFirst()
                            .orElse(null);
                    TicketItem ti = new ProductItem(prod, pa.getCuantity());
                    assignationTicket.getItems().add(ti);
                }

                List<AssignedTickets> listaAssignedTicketsFromDb = session
                        .createQuery("FROM AssignedTickets", AssignedTickets.class)
                        .getResultList();
                for(AssignedTickets at: listaAssignedTicketsFromDb){
                    Cashier cash= Tienda.getCashierById(at.getCashierId());
                    Ticket ticketToAssign= ticketCargados.stream()
                            .filter(tt -> tt.getId().equals(at.getTicketId()))
                            .findFirst()
                            .orElse(null);
                    cash.addTicket(ticketToAssign);
                }


                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido cargar los datos de la base de datos por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }
    }


}


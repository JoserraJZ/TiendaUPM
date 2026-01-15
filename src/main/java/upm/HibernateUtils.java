package upm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import upm.products.CustomizableProduct;
import upm.products.Product;
import upm.products.ProductCampusFood;
import upm.products.ProductMeeting;
import upm.ticketitems.ProductItem;
import upm.ticketitems.ServiceItem;
import upm.ticketitems.TicketItem;

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
        this.instance = this;

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

    public void addClientToDb(Client c) {
        /*if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.merge(c); // Reemplaza a save en Hibernate 7
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
        }*/
    }


    public void addCashierToDb(Cashier ca){
       /* if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.merge(ca); // Reemplaza a save en Hibernate 7
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
        }*/
    }
    public void addServiceToDb(Service s){
        /*if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.merge(s); // Reemplaza a save en Hibernate 7
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
        }*/
    }
    public void addProductToDb(Product p){
       /* if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.merge(p); // Reemplaza a save en Hibernate 7
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
        }*/
    }

    public void addTicketToDb(Ticket t){
        /*if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.merge(t); // Reemplaza a save en Hibernate 7
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
        }*/
    }

    public void addServiceAddedDb(ServiceAdded sA){
        /*if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.merge(sA); // Reemplaza a save en Hibernate 7
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido el servicio añadir a la bd por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }*/
    }

    public void addProductItemtoDb(ProductAdded pa){
        /*if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.merge(pa); // Reemplaza a save en Hibernate 7
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido el producto añadir a la bd por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }*/
    }


    public void endConnection(){
        factory.close();
    }

    public void saveCurrentState(){
        /*if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido guardar el estado actual por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }*/
    }

    public void SaveApp(Catalog<Product> productCatalog, Catalog<Service> serviceCatalog,
                        Set<Cashier> cashiers, Set<Client> clients ) {

        if (factory != null) {
            Session session = factory.getCurrentSession();
            try {
                session.beginTransaction();
                for (Product p : productCatalog.items.values()) {
                    session.merge(p);
                }
                for (Service s : serviceCatalog.items.values()) {
                    session.merge(s);
                }
                for(Client c : clients){
                    session.merge(c);
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

                session.getTransaction().commit();
            } catch (Exception e) {
                if (session.getTransaction() != null && session.getTransaction().isActive()) {
                    session.getTransaction().rollback();
                }
                throw new RuntimeException("No se ha podido guardar el estado actual del catalogo por el error " + e);
            } finally {
                try {
                    if (session.isOpen()) session.close();
                } catch (Exception ignored) {}
            }
        }

    }

}


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
                    .addAnnotatedClass(ServiceAdded.class)
                    .addAnnotatedClass(ProductAdded.class)
                    .addAnnotatedClass(CustomizableProduct.class)
                    .addAnnotatedClass(ProductCampusFood.class)
                    .addAnnotatedClass(ProductMeeting.class)

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



}


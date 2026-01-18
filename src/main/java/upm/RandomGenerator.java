package upm;

import upm.products.Product;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RandomGenerator {
    private static final Random random = new Random();
    private static Catalog<Product> catalog;
    private static Set<Cashier> cashiers;
    private static Set<Client> clients;

    public static void Init(Catalog<Product> productCatalog, Set<Cashier> cashiers, Set<Client> clients) {
        RandomGenerator.catalog = productCatalog;
        RandomGenerator.cashiers = cashiers;
        RandomGenerator.clients = clients;
    }

    public static String generateCashierId() {
        for (int i = 0; i < 1000; i++) { // si en 1000 casos no funciona dar un error
            String possibleId = "UW" + String.format("%07d", random.nextInt(10_000_000));
            boolean exists = false;
            for (Cashier c : cashiers) {
                if (c.getId().equals(possibleId)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                return possibleId;
            }
        }
        throw new RuntimeException("No se pudo generar un ID único en 1000 intentos");
    }

    public static String generateDNI(){
        for (int i = 0; i < 1000; i++) { // si en 1000 casos no funciona dar un error
            String possibleId =  String.format("%08d%c", random.nextInt(100_000_000), (char)('A' + random.nextInt(26)));

            boolean exists = false;
            for (Client c : clients) {
                if (c.getId().equals(possibleId)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                return possibleId;
            }
        }
        throw new RuntimeException("No se pudo generar un ID único en 1000 intentos");
    }

    public static int generateProductId() {
        int id = 0;
        while (id < Integer.MAX_VALUE) {
            if (!catalog.doesIdExist(id)) {
                return id;
            }
            id++;
        }
        throw new RuntimeException("No se pudo generar un ID único: se alcanzó Integer.MAX_VALUE");
    }

    public static String generateTicketId() {

        ///////////////////////////////////////////////////////////////////////////////////
        DateTimeFormatter fixedFmt = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        LocalDateTime fixedDateTime = LocalDateTime.parse("25-12-07-22:32", fixedFmt);
        ///////////////////////////////////////////////////////////////////////////////////

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        //Modificar aqui
        String datePart = fixedDateTime.format(fmt);
        int rand = random.nextInt(100_000); // 0..99999
        String randPart = String.format("%05d", rand);
        return datePart + "-" + randPart;
    }

    private static final Set<Integer> usedServiceIds = new LinkedHashSet<>();

    public static synchronized int generateServiceId() {
        int id = 1;
        while (usedServiceIds.contains(id)) {
            id++;
            if (id == Integer.MAX_VALUE) {
                throw new RuntimeException("No se pudo generar un ID de servicio único: se alcanzó Integer.MAX_VALUE");
            }
        }
        usedServiceIds.add(id);
        return id;
    }
}

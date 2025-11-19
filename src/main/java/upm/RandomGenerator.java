package upm;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Set;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RandomGenerator {
    private static final Random random = new Random();

    private static ProductCatalog catalog;
    private static Set<Cashier> cashiers;
    private static Set<Client> clients;

    public static void Init(ProductCatalog catalog, Set<Cashier> cashiers, Set<Client> clients) {
        RandomGenerator.catalog = catalog;
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
            String possibleId =  String.format("%08d%c", random.nextInt(100_000_000),
                                               (char)('A' + random.nextInt(26)));

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
    public static int generateProductId(){
        for (int i = 0; i < 1000; i++) { // si en 1000 casos no funciona dar un error
            int possibleId = new Random().nextInt(Integer.MAX_VALUE);

            if (!catalog.doesIdExist(possibleId)) {
                return possibleId;
            }
        }
        throw new RuntimeException("No se pudo generar un ID único en 1000 intentos");
    }

    public static String generateTicketId() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yy-MM-dd-HH:mm");
        String datePart = now.format(fmt);
        int rand = random.nextInt(100_000); // 0..99999
        String randPart = String.format("%05d", rand);
        return datePart + "-" + randPart;
    }

}

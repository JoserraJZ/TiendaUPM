package upm;

import upm.products.ProductCategory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Service {

    private final String id;
    private String category;
    private ZonedDateTime expirationDate;

    public Service(String stringId, LocalDateTime maxDate, String category) {
        this.id = stringId; //EMPIEZA POR 1 ACABA POR S
        this.category = category;
        this.expirationDate = maxDate.atZone(ZoneId.of("CET"));;
    }
    @Override
    public String toString() {
        String formattedDate = expirationDate.format(DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy").withLocale(java.util.Locale.ENGLISH));

        return String.format(Locale.US,
                "{class:%s, id:%s, category:%s, expiration:%s}",
                "ProductService", id, category, formattedDate);
    }
}

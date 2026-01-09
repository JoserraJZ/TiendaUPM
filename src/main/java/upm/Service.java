package upm;

import jakarta.persistence.*;

import upm.products.ProductCategory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "service")
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final int id;


    private String category;
    private ZonedDateTime expirationDate;

    public Service(int id, LocalDateTime maxDate, String category) {
        this.id = id; // ahora es un entero secuencial
        this.category = category;
        this.expirationDate = maxDate.atZone(ZoneId.of("CET"));
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public ZonedDateTime getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String toString() {
        String formattedDate = expirationDate.format(
                DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy").withLocale(Locale.ENGLISH)
        );

        return String.format(Locale.US,
                "{class:%s, id:%d, category:%s, expiration:%s}",
                "ProductService", id, category, formattedDate);
    }

    public Service cloneService() {
        Service clone = new Service(
                this.id,                                   // mismo id
                this.expirationDate.toLocalDateTime(),     // misma fecha
                this.category                               // misma categoría
        );
        return clone;
    }

}

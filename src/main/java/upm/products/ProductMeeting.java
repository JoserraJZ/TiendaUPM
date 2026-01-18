package upm.products;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "ProductMeeting")

public class ProductMeeting extends Product {
    private LocalDateTime expirationDateTime;
    private int maxParticipants;

    protected ProductMeeting(){    }

    public ProductMeeting(String stringId, String productName, double pricePerPerson, LocalDateTime expirationDateTime, int maxParticipants) {
        super(stringId, productName, null, pricePerPerson);
        this.expirationDateTime = expirationDateTime;
        this.maxParticipants = Math.min(maxParticipants, 100);
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    @Override
    public ProductMeeting clone() {
        return new ProductMeeting(
                String.valueOf(getId()),
                getName(),
                super.getPrice(),
                getExpirationDateTime(),
                getMaxParticipants()
        );
    }

    public String toParametersString() {
        return String.format(Locale.US,
                "{class:%s, id:%d, name:'%s', price:%%.1f, date of Event:%s, max people allowed:%d%%s}",
                "Meeting", super.getId(), super.getName(), expirationDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxParticipants);

    }

    public String toString(){
        return String.format(Locale.US,
                "{class:%s, id:%d, name:'%s', price:%.1f, date of Event:%s, max people allowed:%d}",
                "Meeting", super.getId(), super.getName(), super.getPrice(), expirationDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxParticipants);

    }
}

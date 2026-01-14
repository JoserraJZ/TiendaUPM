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
    private LocalDateTime creationDateTime;
    private int maxParticipants;

    public ProductMeeting(String stringId, String productName, double pricePerPerson, LocalDateTime expirationDateTime, int maxParticipants, LocalDateTime creationDateTime) {
        super(stringId, productName, null, pricePerPerson);
        this.expirationDateTime = expirationDateTime;
        this.maxParticipants = Math.min(maxParticipants, 100);
        this.creationDateTime = creationDateTime;
    }

    public LocalDateTime getExpirationDateTime() {
        return expirationDateTime;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setExpirationDateTime(LocalDateTime expirationDateTime) {
        this.expirationDateTime = expirationDateTime;
    }

    @Override
    public ProductMeeting clone() {
        ProductMeeting copy = new ProductMeeting(
                String.valueOf(getId()),
                getName(),
                super.getPrice(),
                getExpirationDateTime(),
                getMaxParticipants(),
                getCreationDateTime()
        );
        return copy;
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

package upm.products;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Table(name = "ProductCampusFood")


public class ProductCampusFood extends Product {
    private LocalDateTime expirationDate;
    private int maxParticipants;

    protected ProductCampusFood() {}

    public ProductCampusFood(String stringId, String productName, double pricePerPerson, LocalDateTime expirationDate, int maxParticipants, LocalDateTime creationDateTime) {
        super(stringId, productName, null, pricePerPerson);
        LocalDateTime fechaCreacion= LocalDateTime.now();
        this.expirationDate = expirationDate;
        this.maxParticipants = Math.min(maxParticipants, 100);
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public double calculateCurrentPrice(int participants) {
        return getPrice() * participants;
    }


    @Override
    public double getPrice() {
        return super.getPrice();
    }

    @Override
    public ProductCampusFood clone() {
        ProductCampusFood copy = new ProductCampusFood(
                String.valueOf(getId()),
                getName(),
                super.getPrice(),
                getExpirationDate(),
                getMaxParticipants(),
                LocalDateTime.now()//TODO:CAMBIAR ESTO
        );
        return copy;
    }

    public String toString() {
        int participants = 0;
        if (participants>0){
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', price:%.1f, date of Event:%s, max people allowed:%d, actual people in event:%d}",
                    "Meeting", super.getId(), super.getName(), getPrice()*participants, expirationDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxParticipants, participants);
        }else{
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', price:%.1f, date of Event:%s, max people allowed:%d}",
                    "Food", super.getId(), super.getName(), getPrice(), expirationDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxParticipants);
        }
    }


}


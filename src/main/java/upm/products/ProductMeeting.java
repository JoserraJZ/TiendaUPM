package upm.products;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ProductMeeting extends Product {
    private LocalDateTime expirationDateTime;
    private LocalDateTime creationDateTime;
    private int maxParticipants;
    private int currentParticipants;

    public ProductMeeting(String stringId, String productName, double pricePerPerson, LocalDateTime expirationDateTime, int maxParticipants, LocalDateTime creationDateTime) {
        super(stringId, productName, null, pricePerPerson);
        this.expirationDateTime = expirationDateTime;
        this.maxParticipants = Math.min(maxParticipants, 100);
        this.currentParticipants=0;
        this.creationDateTime = creationDateTime;
    }

    public boolean addParticipants(int participantsAdded){
        if (participantsAdded>maxParticipants){
            return false;
        }else {
            currentParticipants+=participantsAdded;
            return  true;
        }
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

    public double calculateCurrentPrice() {
        return getPrice() * currentParticipants;
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

    @Override
    public String toString() {
        if (currentParticipants>0){
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', price:%.1f, date of Event:%s, max people allowed:%d, actual people in event:%d}",
                    "Meeting", super.getId(), super.getName(), super.getPrice()*currentParticipants, expirationDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxParticipants, currentParticipants);
        }else{
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', price:%.1f, date of Event:%s, max people allowed:%d}",
                    "Meeting", super.getId(), super.getName(), super.getPrice(), expirationDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxParticipants);
        }
    }
}

package upm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ProductCampusFood extends Product {
    private LocalDateTime expirationDate;
    private int maxParticipants;
    private int currentParticipants;


    public ProductCampusFood(String stringId, String productName, double pricePerPerson, LocalDateTime expirationDate, int maxParticipants) {
        super(stringId, productName, null, pricePerPerson);
        LocalDateTime fechaCreacion= LocalDateTime.now();
        this.expirationDate = expirationDate;
        this.maxParticipants = Math.min(maxParticipants, 100);
    }
    public boolean addParticipants(int participantsAdded){

        if (participantsAdded>maxParticipants){
            return false;
        }else {
            currentParticipants+=participantsAdded;
            return  true;
        }
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public double calculateCurrentPrice() {
        return getPrice() * currentParticipants;
    }

    @Override
    public double getPrice() {
        return super.getPrice();
    }

    @Override
    public String toString() {
        if (currentParticipants>0){
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', price:%.1f, date of Event:%s, max people allowed:%d, actual people in event:%d}",
                    "Meeting", super.getId(), super.getName(), super.getPrice()*currentParticipants, expirationDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxParticipants, currentParticipants);
        }else{
            return String.format(Locale.US,
                    "{class:%s, id:%d, name:'%s', price:%.1f, date of Event:%s, max people allowed:%d}",
                    "Food", super.getId(), super.getName(), super.getPrice(), expirationDate.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxParticipants);
        }
    }
}


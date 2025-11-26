package upm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ProductMeeting extends Product {
    private LocalDateTime expirationDateTime;
    private int maxParticipants;
    private int currentParticipants;

    public ProductMeeting(String stringId, String productName, double pricePerPerson, LocalDateTime expirationDateTime, int maxParticipants, LocalDateTime creationDateTime) {
        super(stringId, productName, null, pricePerPerson);
        try {
            if (Duration.between(creationDateTime, expirationDateTime).toHours() < 12) {
                throw new IllegalArgumentException("Las reuniones requieren al menos 12 horas de planificación.");
            }

            this.expirationDateTime = expirationDateTime;
            this.maxParticipants = Math.min(maxParticipants, 100);
            this.currentParticipants=0;
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public boolean addParticipants(int participantsAdded){

        if (participantsAdded>maxParticipants){
            return false;
        }else {
            currentParticipants+=participantsAdded;
            return  true;
        }
    }

    public double calculateCurrentPrice() {
        return getPrice() * currentParticipants;
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
    // Getters y setters si los necesitas
}

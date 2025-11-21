package upm;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ProductMeeting extends Product {
    private LocalDateTime expirationDateTime;
    private int maxParticipants;
    private LocalDateTime creationDateTime;

    public ProductMeeting(String stringId, String productName, double pricePerPerson, LocalDateTime expirationDateTime, int maxParticipants, LocalDateTime creationDateTime) {
        super(stringId, productName, null, pricePerPerson);
        try {
            if (Duration.between(creationDateTime, expirationDateTime).toHours() < 12) {
                throw new IllegalArgumentException("Las reuniones requieren al menos 12 horas de planificación.");
            }

            this.expirationDateTime = expirationDateTime;
            this.maxParticipants = Math.min(maxParticipants, 100);
            this.creationDateTime = LocalDateTime.now();
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public double calculateTotalPrice() {
        return getPrice() * maxParticipants;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "{class:%s, id:%d, name:'%s', price:%.1f, date of Event:%s, max people allowed:%d}",
                "Meeting", super.getId(), super.getName(), super.getPrice(), expirationDateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), maxParticipants);
    }
    // Getters y setters si los necesitas
}

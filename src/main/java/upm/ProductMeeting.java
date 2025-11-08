package main.java.upm;

import java.time.Duration;
import java.time.LocalDateTime;

public class ProductMeeting extends Product {
    private LocalDateTime expirationDateTime;
    private int maxParticipants;
    private LocalDateTime creationDateTime;

    public ProductMeeting(int idProd, String productName, double pricePerPerson, LocalDateTime expirationDateTime, int maxParticipants, LocalDateTime creationDateTime) {
        super(idProd, productName, null, pricePerPerson);
        this.expirationDateTime = expirationDateTime;
        this.maxParticipants = Math.min(maxParticipants, 100);
        this.creationDateTime = creationDateTime;
        validatePlanningTime();
    }

    private void validatePlanningTime() {
        if (Duration.between(creationDateTime, expirationDateTime).toHours() < 12) {
            throw new IllegalArgumentException("Las reuniones requieren al menos 12 horas de planificación.");
        }
    }

    public double calculateTotalPrice() {
        return getPrecio() * maxParticipants;
    }

    // Getters y setters si los necesitas
}

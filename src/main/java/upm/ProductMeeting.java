package upm;

import java.time.Duration;
import java.time.LocalDateTime;

public class ProductMeeting extends Product {
    private LocalDateTime expirationDateTime;
    private int maxParticipants;
    private LocalDateTime creationDateTime;

    public ProductMeeting(String stringId, String productName, double pricePerPerson, LocalDateTime expirationDateTime, int maxParticipants, LocalDateTime creationDateTime) {
        super(stringId, productName, null, pricePerPerson);
        this.expirationDateTime = expirationDateTime;
        this.maxParticipants = Math.min(maxParticipants, 100);
        this.creationDateTime = LocalDateTime.now();
        validatePlanningTime();
    }

    private void validatePlanningTime() {
        if (Duration.between(creationDateTime, expirationDateTime).toHours() < 12) {
            //TODO: HACER QUE ESTO HAGA UN SOUT Y DE ERROR EN VEZ DE UN EXCEPTION
            throw new IllegalArgumentException("Las reuniones requieren al menos 12 horas de planificación.");
        }
    }

    public double calculateTotalPrice() {
        return getPrice() * maxParticipants;
    }

    // Getters y setters si los necesitas
}

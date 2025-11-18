package upm;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ProductCampusFood extends Product {
    private LocalDateTime expirationDate;
    private int maxParticipants;
    private LocalDateTime creationDate;

    public ProductCampusFood(String stringId, String productName, double pricePerPerson, LocalDateTime expirationDate, int maxParticipants) {
        super(stringId, productName, null, pricePerPerson);
        this.expirationDate = expirationDate;
        this.maxParticipants = Math.min(maxParticipants, 100);
        this.creationDate = LocalDateTime.now();
        validatePlanningTime();
    }

    private void validatePlanningTime() {
        if (ChronoUnit.DAYS.between(creationDate, expirationDate) < 3) {
            //TODO: HACER QUE ESTO HAGA UN SOUT Y DE ERROR EN VEZ DE UN EXCEPTION
            throw new IllegalArgumentException("Las comidas requieren al menos 3 días de planificación.");
        }
    }

    public double calculateTotalPrice() {
        return getPrice() * maxParticipants;
    }

    // Getters y setters si los necesitas
}


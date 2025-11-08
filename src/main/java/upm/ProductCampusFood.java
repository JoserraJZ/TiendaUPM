package main.java.upm;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductCampusFood extends Product {
    private LocalDate expirationDate;
    private int maxParticipants;
    private LocalDate creationDate;

    public ProductCampusFood(int idProd, String productName, double pricePerPerson, LocalDate expirationDate, int maxParticipants, LocalDate creationDate) {
        super(idProd, productName, null, pricePerPerson);
        this.expirationDate = expirationDate;
        this.maxParticipants = Math.min(maxParticipants, 100);
        this.creationDate = creationDate;
        validatePlanningTime();
    }

    private void validatePlanningTime() {
        if (ChronoUnit.DAYS.between(creationDate, expirationDate) < 3) {
            throw new IllegalArgumentException("Las comidas requieren al menos 3 días de planificación.");
        }
    }

    public double calculateTotalPrice() {
        return getPrecio() * maxParticipants;
    }

    // Getters y setters si los necesitas
}


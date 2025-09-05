package io.paymeter.assessment.domain.model.valueobject;

import lombok.Getter;

import java.time.Duration;

public class Pricing {

    @Getter
    private final String parkingId;
    private final int pricePerHour;
    private final Integer dailyCap;
    private final Integer capPer12h;
    private final boolean firstHourFree;

    public Pricing(String parkingId, int pricePerHour, Integer dailyCap, Integer capPer12h, boolean firstHourFree) {
        this.parkingId = parkingId;
        this.pricePerHour = pricePerHour;
        this.dailyCap = dailyCap;
        this.capPer12h = capPer12h;
        this.firstHourFree = firstHourFree;
    }

    /**
     * Calcula el precio según la duración.
     */
    public Money calculatePrice(Duration duration) {
        long minutes = duration.toMinutes();

        // Menos de 1 minuto = gratis
        if (minutes < 1) {
            return new Money(0);
        }

        // Convertir minutos a horas, redondeando hacia arriba
        long hours = (long) Math.ceil(minutes / 60.0);

        // Primera hora gratis (Cliente 2)
        if (firstHourFree && hours > 0) {
            hours -= 1;
        }

        // Calcular precio base
        int basePrice = (int) (hours * pricePerHour);

        // Aplicar tope diario (Cliente 1)
        if (dailyCap != null) {
            // días completos
            long fullDays = duration.toDays();
            int remainderHours = (int) (duration.toHours() % 24);

            int total = (int) (fullDays * dailyCap);
            int remainderPrice = remainderHours * pricePerHour;

            if (remainderPrice > dailyCap) {
                remainderPrice = dailyCap;
            }

            basePrice = total + remainderPrice;
        }

        // Aplicar tope cada 12 horas (Cliente 2)
        if (capPer12h != null) {
            long full12hBlocks = hours / 12;
            long remainder = hours % 12;

            int total = (int) (full12hBlocks * capPer12h);
            int remainderPrice = (int) (remainder * pricePerHour);

            if (remainderPrice > capPer12h) {
                remainderPrice = capPer12h;
            }

            basePrice = total + remainderPrice;
        }

        return new Money(basePrice);
    }
}

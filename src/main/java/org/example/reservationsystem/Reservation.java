package org.example.reservationsystem;

import java.time.LocalDate;

public record Reservation(
        Long reservationId,
        Long userId,
        Long roomId,
        LocalDate startDate,
        LocalDate endDate,
        ReservationStatus status
) {
}

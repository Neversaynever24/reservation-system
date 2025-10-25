package org.example.reservationsystem;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {

    private final Map<Long, Reservation> reservationMap;
    private final AtomicLong idCounter;

    public ReservationService() {
        reservationMap = new HashMap<>();
        idCounter = new AtomicLong();
    }

    public Reservation getReservationById(Long reservationId) {

        if (!reservationMap.containsKey(reservationId)) {
            throw new NoSuchElementException("Not found reservation: " + reservationId);
        }
        return reservationMap.get(reservationId);
    }

    public List<Reservation> findAllReservations() {
        return reservationMap.values().stream().toList();
    }

    public Reservation createReservation(Reservation reservationToCreate) {
        if (reservationToCreate.reservationId() != null) {
            throw new IllegalArgumentException("Id should be empty");
        }
        if (reservationToCreate.status() != null) {
            throw new IllegalArgumentException("Status should be empty");
        }

        var newReservation = new Reservation(
                idCounter.incrementAndGet(),
                reservationToCreate.userId(),
                reservationToCreate.roomId(),
                reservationToCreate.startDate(),
                reservationToCreate.endDate(),
                ReservationStatus.PENDING
        );
        reservationMap.put(newReservation.reservationId(), newReservation);
        return newReservation;
    }

    public Reservation updateReservation(Long reservationId, Reservation reservationToUpdate) {
        if (!reservationMap.containsKey(reservationId)) {
            throw new NoSuchElementException("Not found reservation: " + reservationId);
        }

        var oldReservation = reservationMap.get(reservationId);
        if (oldReservation.status() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Status should be PENDING status = " + oldReservation.status());
        }

        var updatedReservation = new Reservation(
                oldReservation.reservationId(),
                reservationToUpdate.userId(),
                reservationToUpdate.roomId(),
                reservationToUpdate.startDate(),
                reservationToUpdate.endDate(),
                ReservationStatus.PENDING
        );

        reservationMap.put(oldReservation.reservationId(), updatedReservation);
        return updatedReservation;
    }

    public Reservation deleteReservation(Long reservationId) {
        if (!reservationMap.containsKey(reservationId)) {
            throw new NoSuchElementException("Not found reservation: " + reservationId);
        }
        return reservationMap.remove(reservationId);
    }

    public Reservation approveReservation(Long reservationId) {
        if (!reservationMap.containsKey(reservationId)) {
            throw new NoSuchElementException("Not found reservation: " + reservationId);
        }

        var pendingReservation = reservationMap.get(reservationId);
        if (pendingReservation.status() != ReservationStatus.PENDING) {
            throw new IllegalArgumentException("Status should be PENDING status = " + pendingReservation.status());
        }

        var isCoflict = isReservationConflict(pendingReservation);
        if (isCoflict) {
            throw new IllegalStateException("Cannot approve reservation: " + reservationId + " because of conflict");
        }

        var approvedReservation = new Reservation(
                pendingReservation.reservationId(),
                pendingReservation.userId(),
                pendingReservation.roomId(),
                pendingReservation.startDate(),
                pendingReservation.endDate(),
                ReservationStatus.APPROVED
        );

        reservationMap.put(pendingReservation.reservationId(), approvedReservation);
        return approvedReservation;
    }

    private boolean isReservationConflict(Reservation reservation) {
        for (Reservation existingReservation : reservationMap.values()) {
            if (existingReservation.reservationId().equals(reservation.reservationId())) {
                continue;
            }
            if (!reservation.roomId().equals(existingReservation.roomId())) {
                continue;
            }
            if (!existingReservation.status().equals(ReservationStatus.APPROVED)) {
                continue;
            }
            if (reservation.startDate().isBefore(existingReservation.startDate())
            && existingReservation.startDate().isBefore(reservation.endDate())) {
                return true;
            }
        }
        return false;
    }
}

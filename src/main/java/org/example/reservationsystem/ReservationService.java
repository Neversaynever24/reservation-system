package org.example.reservationsystem;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class ReservationService {

    private final Map<Long, Reservation> reservationMap = Map.of(
            1L, new Reservation(
                    1L,
                    422L,
                    213L,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    ReservationStatus.APPROVED
            ),
            2L, new Reservation(
                    2L,
                    652L,
                    213L,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    ReservationStatus.APPROVED
            ),
            3L, new Reservation(
                    3L,
                    542L,
                    233L,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    ReservationStatus.APPROVED
            )
    );

    public Reservation getReservationById(Long reservationId) {

        if (!reservationMap.containsKey(reservationId)) {
            throw new NoSuchElementException("Not found reservation: " + reservationId);
        }
        return reservationMap.get(reservationId);
    }

    public List<Reservation> findAllReservations() {
        return reservationMap.values().stream().toList();
    }
}

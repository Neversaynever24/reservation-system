package org.example.reservationsystem;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable("reservationId") Long reservationId
    ) {
        log.info("called getReservationById{}", reservationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(reservationService.getReservationById(reservationId));
    }

    @GetMapping()
    public ResponseEntity<List<Reservation>> getAllReservations(
    ) {
        log.info("called getAllReservations");
        return ResponseEntity.ok(reservationService.findAllReservations());
    }

    @PostMapping()
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservationToCreate
    ) {
        log.info("called createReservation");
        return ResponseEntity.status(201)
                .body(reservationService.createReservation(reservationToCreate));
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable("reservationId") Long reservationid,
            @RequestBody Reservation reservationToUpdate
    ) {
        log.info("called updateReservation id={}, reservationToUpdate={}", reservationid, reservationToUpdate);
        Reservation updated = reservationService.updateReservation(reservationid, reservationToUpdate);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<Reservation> deleteReservation(
            @PathVariable("reservationId") Long reservationId
    ) {
        log.info("called deleteReservation");
        return ResponseEntity.ok(reservationService.deleteReservation(reservationId));
    }
}

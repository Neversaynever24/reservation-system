package org.example.reservationsystem;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReservationController {
    private final ReservationService reservationService;

    private static final Logger log = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/{reservationId}")
    public Reservation getReservationById(
            @PathVariable("reservationId") Long reservationId
    ) {
        log.info("called getReservationById" + reservationId);
        return reservationService.getReservationById(reservationId);
    }

    @GetMapping()
    public List<Reservation> getAllReservations(
    ) {
        log.info("called getAllReservations");
        return reservationService.findAllReservations();
    }

    @PostMapping()
    public Reservation createReservation(
            @RequestBody Reservation reservationToCreate
    ) {
        log.info("called createReservation");
        return reservationService.createReservation(reservationToCreate);
    }
}

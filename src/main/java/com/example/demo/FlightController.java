package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class FlightController {

    private static final AtomicLong idGenerator = new AtomicLong(3);

    // Емуляція бази даних
    private static final List<Flight> flights = new CopyOnWriteArrayList<>(List.of(
            new Flight(1L, "Santiago", "Sydney",
                    LocalDateTime.of(2026, 11, 19, 6, 15),
                    LocalDateTime.of(2026, 11, 19, 2, 6),
                    new BigDecimal("993.00"), "LA809", LocalDateTime.now()),
            new Flight(2L, "Oslo", "Seattle",
                    LocalDateTime.of(2026, 11, 19, 8, 34),
                    LocalDateTime.of(2026, 11, 19, 0, 21),
                    new BigDecimal("1100"), "VJT809", LocalDateTime.now()),
            new Flight(3L, "Lhasa", "Chengdu",
                    LocalDateTime.of(2026, 11, 19, 10, 55),
                    LocalDateTime.of(2026, 11, 19, 8, 0),
                    new BigDecimal("203"), "3U8700", LocalDateTime.now())
    ));

    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights() {
        return ResponseEntity.status(HttpStatus.OK).body(flights);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> getFlightById(@PathVariable Long id) {
        Optional<Flight> flight = findById(id);
        return flight
                .map(f -> ResponseEntity.status(HttpStatus.OK).body(f))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/flights")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        flight.setId(idGenerator.incrementAndGet());
        flight.setCreationDate(LocalDateTime.now());
        flights.add(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(flight);
    }

    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        boolean removed = flights.removeIf(f -> f.getId().equals(id));
        return removed
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    private Optional<Flight> findById(Long id) {
        return flights.stream().filter(f -> f.getId().equals(id)).findFirst();
    }
}

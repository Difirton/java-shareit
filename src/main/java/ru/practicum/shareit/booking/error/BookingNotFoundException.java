package ru.practicum.shareit.booking.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(Long id) {
        super(String.format("Booking with id = %s, not found", id));
        log.error("Booking with id = {}, not found", id);
    }
}

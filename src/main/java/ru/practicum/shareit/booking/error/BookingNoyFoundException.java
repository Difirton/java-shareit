package ru.practicum.shareit.booking.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookingNoyFoundException extends RuntimeException {

    public BookingNoyFoundException(Long id) {
        super(String.format("Booking with id = %s, not found", id));
        log.error("Booking with id = {}, not found", id);
    }

    public BookingNoyFoundException(Long id, Long userId) {
        super(String.format("User with id = %s hasn't booking with id = %s", userId, id));
        log.error("User with id = {} hasn't booking with id = {}", userId, id);
    }
}

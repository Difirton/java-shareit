package ru.practicum.shareit.booking.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.web.convertor.BookingDtoToBookingConverter;
import ru.practicum.shareit.booking.web.convertor.BookingToBookingDtoConverter;
import ru.practicum.shareit.booking.web.dto.BookingDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {
    private final BookingService bookingService;
    private final BookingToBookingDtoConverter bookingToBookingDtoConverter;
    private final BookingDtoToBookingConverter bookingDtoToBookingConverter;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    BookingDto createBooking(@RequestBody @Valid BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        bookingDto.setRenterId(userId);
        Booking newBooking = bookingService.save(bookingDtoToBookingConverter.convert(bookingDto));
        return bookingToBookingDtoConverter.convert(newBooking);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<BookingDto> getBookings(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<Booking> allBookings = bookingService.findAll(userId);
        return allBookings.stream()
                .map(bookingToBookingDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookingDto getBookingById(@PathVariable("id") Long id) {
        return bookingToBookingDtoConverter.convert(bookingService.findById(id));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookingDto updateBookingStatus(@PathVariable("id") Long id, @RequestParam("approved") Boolean isApproved) {
        Booking booking = bookingService.updateStatus(id, isApproved);
        return bookingToBookingDtoConverter.convert(booking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteBooking(@PathVariable("id") Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        bookingService.deleteById(id, userId);
    }
}

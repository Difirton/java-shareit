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
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {
    private final BookingService bookingService;
    private final BookingToBookingDtoConverter bookingToBookingDtoConverter;
    private final BookingDtoToBookingConverter bookingDtoToBookingConverter;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    BookingDto createBooking(@RequestBody @Valid BookingDto bookingDto,
                             @RequestHeader(USER_REQUEST_HEADER) Long renterId) {
        bookingDto.setRenterId(renterId);
        Booking newBooking = bookingService.save(bookingDtoToBookingConverter.convert(bookingDto), renterId);
        return bookingToBookingDtoConverter.convert(newBooking);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<BookingDto> getBookings(@RequestHeader(USER_REQUEST_HEADER) Long userId,
                                 @RequestParam(value = "state") Optional<String> stateName,
                                 @RequestParam Optional<Integer> from,
                                 @RequestParam Optional<Integer> size) {
        return bookingService.findAllByRenterId(userId, stateName.orElse("ALL"), from.orElse(0),
                        size.orElse(20)).stream()
                                                .map(bookingToBookingDtoConverter::convert)
                                                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    List<BookingDto> getOwnersBookingsByState(@RequestHeader(USER_REQUEST_HEADER) Long userId,
                                              @RequestParam(value = "state") Optional<String> stateName,
                                              @RequestParam Optional<Integer> from,
                                              @RequestParam Optional<Integer> size) {
        return bookingService.findAllByOwnerId(userId, stateName.orElse("ALL"), from.orElse(0),
                        size.orElse(20)).stream()
                .map(bookingToBookingDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookingDto getBookingById(@PathVariable("id") Long id, @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return bookingToBookingDtoConverter.convert(bookingService.findById(id, userId));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookingDto updateBookingStatus(@PathVariable("id") Long id, @RequestHeader(USER_REQUEST_HEADER) Long ownerId,
                                   @RequestParam("approved") Boolean isApproved) {
        Booking booking = bookingService.updateStatus(id, ownerId, isApproved);
        return bookingToBookingDtoConverter.convert(booking);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteBooking(@PathVariable("id") Long id, @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        bookingService.deleteById(id, userId);
    }
}
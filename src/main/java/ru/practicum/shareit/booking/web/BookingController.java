package ru.practicum.shareit.booking.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.web.convertor.BookingDtoToBookingConverter;
import ru.practicum.shareit.booking.web.convertor.BookingToBookingDtoConverter;
import ru.practicum.shareit.booking.web.dto.BookingDto;
import ru.practicum.shareit.user.web.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "The booking API", description = "API for interacting with endpoints associated with bookings")
@RequestMapping(path = "/bookings", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingController {
    private final BookingService bookingService;
    private final BookingToBookingDtoConverter bookingToBookingDtoConverter;
    private final BookingDtoToBookingConverter bookingDtoToBookingConverter;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

    @Operation(summary = "Creates a new user's booking by user id, which is specified in header",
            tags = "The booking API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The booking was created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not valid parameters"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    BookingDto createBooking(@RequestBody @Valid BookingDto bookingDto,
                             @Parameter(description = "Renter ID") @RequestHeader(USER_REQUEST_HEADER) Long renterId) {
        bookingDto.setRenterId(renterId);
        Booking newBooking = bookingService.save(bookingDtoToBookingConverter.convert(bookingDto), renterId);
        return bookingToBookingDtoConverter.convert(newBooking);
    }

    @Operation(summary = "Get the user's bookings by user id, which is specified in header", tags = "The booking API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the requested bookings",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<BookingDto> getBookings(@Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId,
                                 @Parameter(description = "State of booking")
                                 @RequestParam(value = "state") Optional<String> stateName,
                                 @RequestParam Optional<Integer> from,
                                 @RequestParam Optional<Integer> size) {
        return bookingService.findAllByRenterId(userId, stateName.orElse("ALL"), from.orElse(0),
                        size.orElse(20)).stream()
                                                .map(bookingToBookingDtoConverter::convert)
                                                .collect(Collectors.toList());
    }

    @Operation(summary = "Get the bookings by owner item id and state, which is specified in URL",
            tags = "The booking API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The booking was removed",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    List<BookingDto> getOwnersBookingsByState(@Parameter(description = "User ID")
                                              @RequestHeader(USER_REQUEST_HEADER) Long userId,
                                  @Parameter(description = "State of booking")
                                              @RequestParam(value = "state") Optional<String> stateName,
                                              @RequestParam Optional<Integer> from,
                                              @RequestParam Optional<Integer> size) {
        return bookingService.findAllByOwnerId(userId, stateName.orElse("ALL"), from.orElse(0),
                        size.orElse(20)).stream()
                .map(bookingToBookingDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get the booking by it's id, which is specified in URL", tags = "The booking API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the requested booking",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookingDto getBookingById(@Parameter(description = "Booking ID") @PathVariable("id") Long id,
                              @Parameter(description = "User ID")
                              @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return bookingToBookingDtoConverter.convert(bookingService.findById(id, userId));
    }

    @Operation(summary = "Update the booking by it's id, which is specified in URL", tags = "The booking API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The booking was updated",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "403", description = "Do not have update permission")
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    BookingDto updateBookingStatus(@PathVariable("id") Long id, @Parameter(description = "Owner ID")
                                   @RequestHeader(USER_REQUEST_HEADER) Long ownerId,
                                   @Parameter(description = "Is booking approved")
                                   @RequestParam("approved") Boolean isApproved) {
        Booking booking = bookingService.updateStatus(id, ownerId, isApproved);
        return bookingToBookingDtoConverter.convert(booking);
    }

    @Operation(summary = "Delete the booking by it's id, which is specified in URL", tags = "The booking API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The booking was removed",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Booking not found"),
            @ApiResponse(responseCode = "403", description = "Do not have delete permission")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteBooking(@PathVariable("id") Long id,
                       @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        bookingService.deleteById(id, userId);
    }
}
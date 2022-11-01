package ru.practicum.shareit.booking.service;

import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.repository.Booking;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface BookingService {
    Booking save(@Valid Booking booking, Long renterId);

    List<Booking> findAllByRenterId(Long userId, String stateName);

    List<Booking> findAllByOwnerId(Long userId, String stateName);

    Booking findById(Long id, Long userId);

    Booking updateStatus(Long id, Long ownerId, Boolean isApproved);

    void deleteById(Long id, Long userId);
}

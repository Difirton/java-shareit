package ru.practicum.shareit.booking.service;

import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.constant.Status;

import javax.validation.Valid;
import java.util.List;

@Validated
public interface BookingService {
    Booking save(@Valid Booking convert);

    List<Booking> findAllByRenterId(Long userId, Status status);

    List<Booking> findAllByOwnerId(Long userId, Status status);

    Booking findById(Long id);

    Booking updateStatus(Long id, Boolean isApproved);

    void deleteById(Long id, Long userId);
}

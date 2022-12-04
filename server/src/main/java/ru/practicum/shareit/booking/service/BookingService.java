package ru.practicum.shareit.booking.service;

import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.constant.Status;

import java.util.List;
import java.util.Optional;

@Validated
public interface BookingService {
    Booking save(Booking booking, Long renterId);

    List<Booking> findAllByRenterId(Long renterId, String stateName, Integer from, Integer size);

    List<Booking> findAllByOwnerId(Long ownerId, String stateName, Integer from, Integer size);

    Booking findById(Long id, Long ownerId);

    Booking updateStatus(Long id, Long ownerId, Boolean isApproved);

    void deleteById(Long id, Long renterId);

    Optional<Booking> findLastBookingByItemIdAndStatuses(Long itemId, List<Status> statuses);

    Optional<Booking> findNextBookingByItemIdAndStatuses(Long itemId, List<Status> statuses);
}
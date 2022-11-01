package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.repository.constant.Status;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByRenterIdOrderByStartDesc(Long ownerId);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByRenterIdAndStatusOrderByStartDesc(Long ownerId, Status status);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, Status status);
}

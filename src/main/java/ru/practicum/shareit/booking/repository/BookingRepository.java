package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.repository.constant.Status;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b JOIN b.item i " +
            "WHERE b.id = ?1 AND (i.owner.id = ?2 OR b.renter.id = ?2)")
    Optional<Booking> findByIdAndItemOwnerIdOrRenterId(Long id, Long userId);

    Optional<Booking> findByIdAndItemOwnerId(Long id, Long ownerId);

    List<Booking> findAllByRenterIdOrderByStartDesc(Long ownerId);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId);

    List<Booking> findAllByRenterIdAndStatusOrderByStartDesc(Long ownerId, Status status);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, Status status);
}

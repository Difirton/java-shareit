package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.repository.constant.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT b FROM Booking b JOIN b.item i " +
            "WHERE b.id = ?1 AND (i.owner.id = ?2 OR b.renter.id = ?2)")
    Optional<Booking> findByIdAndItemOwnerIdOrRenterId(Long id, Long userId);

    Optional<Booking> findByIdAndItemOwnerId(Long id, Long ownerId);

    List<Booking> findAllByRenterIdOrderByStartDesc(Long ownerId);

    List<Booking> findAllByRenterIdOrderByStartDesc(Long ownerId, Pageable pageable);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long userId, Pageable pageable);

    List<Booking> findAllByRenterIdAndStatusOrderByStartDesc(Long ownerId, Status status);

    List<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(Long userId, Status status);

    List<Booking> findAllByItemIdOrderByStart(Long id);

    Optional<Booking> findFirstByItemIdAndRenterIdAndStatusAndFinishBefore(Long itemId, Long renterId, Status status,
                                                                          LocalDateTime now);

    List<Booking> findAllByRenterIdAndStartBeforeAndFinishAfterOrderByStartDesc(Long renterId,
                                                                                LocalDateTime startBefore,
                                                                                LocalDateTime finishAfter);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndFinishAfterOrderByStartDesc(Long ownerId,
                                                                                   LocalDateTime startBefore,
                                                                               LocalDateTime finishAfter);

    List<Booking> findAllByItemOwnerIdAndFinishBeforeOrderByStartDesc(Long ownerId, LocalDateTime finishBefore);

    List<Booking> findAllByRenterIdAndFinishBeforeOrderByStartDesc(Long renterId, LocalDateTime finishBefore);

    Optional<Booking> findTopByItemIdAndStartAfterAndStatusInOrderByStart(Long itemId, LocalDateTime finishAfter,
                                                                               List<Status> statuses);

    Optional<Booking> findTopByItemIdAndFinishBeforeAndStatusInOrderByFinishDesc(Long id, LocalDateTime finishBefore,
                                                                            List<Status> statuses);
}

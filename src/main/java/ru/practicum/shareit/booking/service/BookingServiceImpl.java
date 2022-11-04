package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.error.BookingNotFoundException;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.constant.State;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.common.utill.NotNullPropertiesCopier;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.repository.constant.State.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService, NotNullPropertiesCopier<Booking> {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public Booking save(Booking booking, Long renterId) {
        if (booking.getStart() != null && booking.getStart().isBefore(booking.getFinish())) {
            booking.setRenter(userService.findById(booking.getRenter().getId()));
            booking.setItem(itemService.findAvailableRenter(booking.getItem().getId(), renterId));
            booking.setStatus(Status.WAITING);
            return bookingRepository.save(booking);
        } else {
            log.error("Attempt to add booking with start = {}, which is later than finish {}",
                    booking.getStart(), booking.getFinish());
            throw new ValidationException("Star of booking must be after finish");
        }
    }

    @Override
    public List<Booking> findAllByRenterId(Long userId, String stateName) {
        switch (this.checkInputStateData(userId, stateName)) {
            case ALL:
                return bookingRepository.findAllByRenterIdOrderByStartDesc(userId);
            case WAITING:
                return bookingRepository.findAllByRenterIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByRenterIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case FUTURE:
                return bookingRepository.findAllByRenterIdOrderByStartDesc(userId).stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByRenterIdAndStartBeforeAndFinishAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByRenterIdAndFinishBeforeOrderByStartDesc(userId, LocalDateTime.now());
            default:
                log.error("Request findAllByRenterId() with renter id = {} wrong state name: {}", userId, stateName);
                throw new IllegalStateException("Unknown state: " + stateName);
        }
    }

    @Override
    public List<Booking> findAllByOwnerId(Long userId, String stateName) {
        switch (this.checkInputStateData(userId, stateName)) {
            case ALL:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
            case WAITING:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
            case FUTURE:
                return bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId).stream()
                        .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByItemOwnerIdAndStartBeforeAndFinishAfterOrderByStartDesc(userId,
                        LocalDateTime.now(), LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByItemOwnerIdAndFinishBeforeOrderByStartDesc(userId,
                        LocalDateTime.now());
            default:
                log.error("Request findAllByOwnerId() with owner id = {} wrong state name: {}", userId, stateName);
                throw new IllegalStateException("Unknown state: " + stateName);
        }
    }

    private State checkInputStateData(Long userId, String stateName) {
        State state = UNSUPPORTED;
        userService.findById(userId);
        if (Arrays.stream(State.values()).anyMatch(s -> s.toString().equals(stateName))) {
            state = State.valueOf(stateName);
        }
        return state;
    }

    @Override
    public Booking findById(Long id, Long ownerId) {
        userService.findById(ownerId);
        return bookingRepository.findByIdAndItemOwnerIdOrRenterId(id, ownerId)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }

    @Override
    public Booking updateStatus(Long id, Long ownerId, Boolean isApproved) {
        Booking booking = bookingRepository.findByIdAndItemOwnerId(id, ownerId)
                .orElseThrow(() -> new BookingNotFoundException(id));
        if (Boolean.TRUE.equals(isApproved) && booking.getStatus() != Status.APPROVED) {
            booking.setStatus(Status.APPROVED);
        } else if (Boolean.FALSE.equals(isApproved) && booking.getStatus() != Status.REJECTED) {
            booking.setStatus(Status.REJECTED);
        } else {
            throw new IllegalStateException("Bad approve request");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteById(Long id, Long userId) {
        bookingRepository.deleteById(id);
    }

    @Override
    public Optional<Booking> findLastBookingByItemIdAndStatuses(Long itemId, List<Status> statuses) {
        return bookingRepository.findTopByItemIdAndFinishBeforeAndStatusInOrderByFinishDesc(itemId, LocalDateTime.now(),
                statuses);
    }

    @Override
    public Optional<Booking> findNextBookingByItemIdAndStatuses(Long itemId, List<Status> statuses) {
        return bookingRepository.findTopByItemIdAndStartAfterAndStatusInOrderByStart(itemId, LocalDateTime.now(),
                statuses);
    }
}

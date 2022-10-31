package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.error.BookingNotFoundException;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.common.utill.NotNullPropertiesCopier;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService, NotNullPropertiesCopier<Booking> {
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public Booking save(Booking booking) {
        if (booking.getStart() != null && booking.getStart().isBefore(booking.getFinish())) {
            booking.setRenter(userService.findById(booking.getRenter().getId()));
            booking.setItem(itemService.findAvailableById(booking.getItem().getId()));
            booking.setStatus(Status.WAITING);
            return bookingRepository.save(booking);
        } else {
            log.error("Attempt to add booking with start = {}, which is later than finish {}",
                    booking.getStart(), booking.getFinish());
            throw new ValidationException("Star of booking must be after finish");
        }
    }

    @Override
    public List<Booking> findAll(Long userId) {
        return bookingRepository.findAllByRenterId(userId);
    }
//TODO переделать
    @Override
    public List<Booking> findAll(Long userId, Status status) {
        return bookingRepository.findAllByRenterId(userId);
    }

    @Override
    public Booking findById(Long id) {
        return bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
    }

    @Override
    public Booking updateStatus(Long id, Boolean isApproved) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException(id));
        if (Boolean.TRUE.equals(isApproved)) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteById(Long id, Long userId) {
        bookingRepository.deleteById(id);
    }
}

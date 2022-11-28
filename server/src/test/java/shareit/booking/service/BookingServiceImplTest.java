package shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.error.BookingNotFoundException;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.repository.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class BookingServiceImplTest {
    private Booking booking;
    User owner;
    User renter;

    @Autowired
    private BookingService bookingService;

    @MockBean
    private UserService userService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private BookingRepository mockRepository;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .build();
        renter = User.builder()
                .id(2L)
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("test")
                .description("testDesc")
                .available(true)
                .owner(owner)
                .build();
        booking = Booking.builder()
                .id(1L)
                .start(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
                .finish(LocalDateTime.of(2020, 2, 1, 1, 1, 1))
                .status(Status.WAITING)
                .renter(renter)
                .item(item)
                .build();
        when(userService.findById(1L)).thenReturn(owner);
        when(userService.findById(2L)).thenReturn(renter);
        when(itemService.findById(1L)).thenReturn(item);
        when(mockRepository.save(booking)).thenReturn(booking);
        when(mockRepository.findByIdAndItemOwnerIdOrRenterId(1L, 1L)).thenReturn(Optional.of(booking));
        when(mockRepository.findByIdAndItemOwnerId(1L, 1L)).thenReturn(Optional.of(booking));
        when(mockRepository.findById(1L)).thenReturn(Optional.of(booking));
    }

    @Test
    @DisplayName("Create new booking, expected OK")
    void testSave() {
        Booking savedBooking = bookingService.save(booking, 2L);
        assertEquals(1L, savedBooking.getId());
        assertEquals(LocalDateTime.of(2020, 1, 1, 1, 1, 1), booking.getStart());
        assertEquals(LocalDateTime.of(2020, 2, 1, 1, 1, 1), booking.getFinish());
        assertEquals(Status.WAITING, savedBooking.getStatus());
        verify(mockRepository, times(1)).save(booking);
    }

    @Test
    @DisplayName("Create new no valid booking, expected ValidationException")
    void testNoValidSave() {
        booking.setStart(LocalDateTime.now());
        booking.setFinish(LocalDateTime.now().minusDays(1));
        assertThrows(ValidationException.class, () -> bookingService.save(booking, 2L));
    }

    @Test
    @DisplayName("Find all bookings, expected OK")
    void testFindAll() {
        List<Booking> bookings = List.of(
                booking,
                Booking.builder()
                        .id(2L)
                        .start(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                        .finish(LocalDateTime.of(2021, 2, 1, 1, 1, 1))
                        .status(Status.APPROVED)
                        .build()
        );
        when(mockRepository.findAll()).thenReturn(bookings);

        List<Booking> returnedBookings = mockRepository.findAll();
        assertEquals(booking, returnedBookings.get(0));
        assertEquals(2L, returnedBookings.get(1).getId());
        assertEquals(LocalDateTime.of(2021, 1, 1, 1, 1, 1),
                returnedBookings.get(1).getStart());
        assertEquals(LocalDateTime.of(2021, 2, 1, 1, 1, 1),
                returnedBookings.get(1).getFinish());
        assertEquals(Status.APPROVED, returnedBookings.get(1).getStatus());
        verify(mockRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Find all bookings, expected OK")
    void testFindById() {
        Booking returnedBooking = bookingService.findById(1L, 1L);
        assertEquals(1L, returnedBooking.getId());
        assertEquals(LocalDateTime.of(2020, 1, 1, 1, 1, 1), returnedBooking.getStart());
        assertEquals(LocalDateTime.of(2020, 2, 1, 1, 1, 1), returnedBooking.getFinish());
        assertEquals(Status.WAITING, returnedBooking.getStatus());
        verify(mockRepository, times(1)).findByIdAndItemOwnerIdOrRenterId(1L, 1L);
    }

    @Test
    @DisplayName("Find bookings by Id, expected Booking Not Found Exception")
    void testFindByIdNotFound() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.findById(2L, 1L));
    }

    @Test
    @DisplayName("Update booking status to approved, expected OK")
    void testUpdateStatusApproved() {
        bookingService.updateStatus(1L, 1L, true);
        assertEquals(Status.APPROVED, booking.getStatus());
        verify(mockRepository, times(1)).save(booking);
    }

    @Test
    @DisplayName("Update booking status to rejected, expected OK")
    void testUpdateStatusRejected() {
        bookingService.updateStatus(1L, 1L, false);
        assertEquals(Status.REJECTED, booking.getStatus());
        verify(mockRepository, times(1)).save(booking);
    }

    @Test
    @DisplayName("Update booking status to rejected, expected Booking Not Found Exception")
    void testUpdateStatusNotFound() {
        assertThrows(BookingNotFoundException.class, () -> bookingService.updateStatus(2L, 1L, true));
    }

    @Test
    @DisplayName("Delete item by Id, expected OK")
    void testDeleteById() {
        bookingService.deleteById(1L, 1L);
        verify(mockRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Test case find all by renter id, expected OK")
    void testCaseFindAllByRenterId() {
        when(mockRepository.findAll(PageRequest.of(0, 10, Sort.by("start").descending())))
                .thenReturn(Page.empty());
        bookingService.findAllByRenterId(2L, "ALL", 0, 10);
        verify(mockRepository, times(1))
                .findAll(PageRequest.of(0, 10, Sort.by("start").descending()));
    }

    @Test
    @DisplayName("Test case find all by renter id, expected OK")
    void testCaseFindAllByRenterId2() {
        bookingService.findAllByRenterId(2L, "WAITING", 0, 10);
        verify(mockRepository, times(1))
                .findAllByRenterIdAndStatusOrderByStartDesc(2L, Status.WAITING);
    }

    @Test
    @DisplayName("Test case find all by renter id, expected OK")
    void testCaseFindAllByRenterId3() {
        bookingService.findAllByRenterId(2L, "REJECTED", 0, 10);
        verify(mockRepository, times(1))
                .findAllByRenterIdAndStatusOrderByStartDesc(2L, Status.REJECTED);
    }

    @Test
    @DisplayName("Test case find all by renter id, expected OK")
    void testCaseFindAllByRenterId4() {
        bookingService.findAllByRenterId(2L, "FUTURE", 0, 10);
        verify(mockRepository, times(1))
                .findAllByRenterIdOrderByStartDesc(2L, PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("Test case find all by renter id, expected OK")
    void testCaseFindAllByRenterId5() {
        bookingService.findAllByRenterId(2L, "CURRENT", 0, 10);
        verify(mockRepository, times(1))
                .findAllByRenterIdAndStartBeforeAndFinishAfterOrderByStartDesc(anyLong(), any(LocalDateTime.class),
                        any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Test case find all by renter id, expected OK")
    void testCaseFindAllByRenterId6() {
        bookingService.findAllByRenterId(2L, "PAST", 0, 10);
        verify(mockRepository, times(1))
                .findAllByRenterIdAndFinishBeforeOrderByStartDesc(anyLong(), isA(LocalDateTime.class));
    }

    @Test
    @DisplayName("Test case find all by renter id, expected IllegalStateException")
    void testNoValidFindAllByRenterId() {
        assertThrows(IllegalStateException.class, () -> bookingService.findAllByRenterId(2L, "ALL", -2, -2));
    }

    @Test
    @DisplayName("Test case find all by renter id, expected IllegalStateException")
    void testNoValidFindAllByRenterId2() {
        assertThrows(IllegalStateException.class, () -> bookingService.findAllByRenterId(2L, "ADfdaf", 2, 2));
    }

    @Test
    @DisplayName("Test case find all by owner id, expected OK")
    void testCaseFindAllByOwnerId() {
        when(mockRepository.findAllByItemOwnerIdOrderByStartDesc(2L, PageRequest.of(0, 10)))
                .thenReturn(new ArrayList<>());
        bookingService.findAllByOwnerId(2L, "ALL", 0, 10);
        verify(mockRepository, times(1))
                .findAllByItemOwnerIdOrderByStartDesc(2L, PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("Test case find all by owner id, expected OK")
    void testCaseFindAllByOwnerId2() {
        bookingService.findAllByOwnerId(2L, "WAITING", 0, 10);
        verify(mockRepository, times(1))
                .findAllByItemOwnerIdAndStatusOrderByStartDesc(2L, Status.WAITING);
    }

    @Test
    @DisplayName("Test case find all by owner id, expected OK")
    void testCaseFindAllByOwnerId3() {
        bookingService.findAllByOwnerId(2L, "REJECTED", 0, 10);
        verify(mockRepository, times(1))
                .findAllByItemOwnerIdAndStatusOrderByStartDesc(2L, Status.REJECTED);
    }

    @Test
    @DisplayName("Test case find all by owner id, expected OK")
    void testCaseFindAllByOwnerId4() {
        bookingService.findAllByOwnerId(2L, "FUTURE", 0, 10);
        verify(mockRepository, times(1))
                .findAllByItemOwnerIdOrderByStartDesc(2L, PageRequest.of(0, 10));
    }

    @Test
    @DisplayName("Test case find all by owner id, expected OK")
    void testCaseFindAllByOwnerId5() {
        bookingService.findAllByOwnerId(2L, "CURRENT", 0, 10);
        verify(mockRepository, times(1))
                .findAllByItemOwnerIdAndStartBeforeAndFinishAfterOrderByStartDesc(anyLong(), isA(LocalDateTime.class),
                        isA(LocalDateTime.class));
    }

    @Test
    @DisplayName("Test case find all by owner id, expected OK")
    void testCaseFindAllByOwnerId6() {
        bookingService.findAllByOwnerId(2L, "PAST", 0, 10);
        verify(mockRepository, times(1))
                .findAllByItemOwnerIdAndFinishBeforeOrderByStartDesc(anyLong(), isA(LocalDateTime.class));
    }

    @Test
    @DisplayName("Test case find all by renter id, expected IllegalStateException")
    void testNoValidFindAllByOwnerId() {
        assertThrows(IllegalStateException.class, () -> bookingService.findAllByOwnerId(2L, "ADfdaf", 2, 2));
    }

    @Test
    @DisplayName("Test case find all by renter id, expected IllegalStateException")
    void testNoValidFindAllByOwnerId2() {
        assertThrows(IllegalStateException.class, () -> bookingService.findAllByOwnerId(2L, "ALL", -2, -2));
    }

    @Test
    @DisplayName("Test find last booking by item id and statuses, expected OK")
    void testFindLastBookingByItemIdAndStatuses() {
        bookingService.findLastBookingByItemIdAndStatuses(2L, new ArrayList<>());
        verify(mockRepository, times(1))
                .findTopByItemIdAndFinishBeforeAndStatusInOrderByFinishDesc(anyLong(), isA(LocalDateTime.class),
                        anyList());
    }

    @Test
    @DisplayName("Test find next booking by item id and statuses, expected OK")
    void testFindNextBookingByItemIdAndStatuses() {
        bookingService.findNextBookingByItemIdAndStatuses(2L, new ArrayList<>());
        verify(mockRepository, times(1))
                .findTopByItemIdAndStartAfterAndStatusInOrderByStart(anyLong(), isA(LocalDateTime.class), anyList());
    }
}
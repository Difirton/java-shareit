package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item_request.repository.ItemRequest;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserTest {
    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("test")
                .email("test@test.ru")
                .items(new ArrayList<>())
                .bookings(new ArrayList<>())
                .itemRequests(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Test add item")
    void testAddItem() {
        Item item = Item.builder()
                .name("test")
                .id(1L)
                .build();
        user.addItem(item);
        assertEquals(item, user.getItems().get(0));
        assertEquals(user, item.getOwner());
    }

    @Test
    @DisplayName("Test add item request")
    void addItemRequest() {
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .description("test")
                .build();
        user.addItemRequest(itemRequest);
        assertEquals(itemRequest, user.getItemRequests().get(0));
        assertEquals(user, itemRequest.getUser());
    }

    @Test
    @DisplayName("Test add booking")
    void addBooking() {
        Booking booking = Booking.builder()
                .id(1L)
                .status(Status.APPROVED)
                .build();
        user.addBooking(booking);
        assertEquals(booking, user.getBookings().get(0));
        assertEquals(user, booking.getRenter());
    }
}
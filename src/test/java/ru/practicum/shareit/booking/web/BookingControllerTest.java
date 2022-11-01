package ru.practicum.shareit.booking.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.user.repository.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookingControllerTest {
    private static final ObjectMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();
    private Booking booking;
    private User user;
    private Item item;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookingService mockService;

    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .build();
        item = Item.builder()
                .id(1L)
                .name("test")
                .description("testDesc")
                .available(true)
                .owner(user)
                .build();
        booking = Booking.builder()
                .id(1L)
                .status(Status.WAITING)
                .start(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
                .finish(LocalDateTime.of(2020, 2, 1, 1, 1, 1))
                .renter(user)
                .item(item)
                .build();
        when(mockService.findById(1L)).thenReturn(booking);
    }

    @Test
    @DisplayName("Request POST /bookings, expected host answer CREATED")
    void testCreateBooking_CREATED_201() throws Exception {
        Booking newBooking = Booking.builder()
                .id(2L)
                .status(Status.WAITING)
                .renter(user)
                .item(item)
                .build();
        when(mockService.save(any(Booking.class), renterId)).thenReturn(newBooking);

        mockMvc.perform(post("/bookings")
                        .content(jsonMapper.writeValueAsString(newBooking))
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.status", is(Status.WAITING.toString())));
        verify(mockService, times(1)).save(any(Booking.class), renterId);
    }

    @Test
    @DisplayName("Request GET /bookings, expected host answer OK")
    void testGetBookings_OK_200() throws Exception {
        List<Booking> bookings = List.of(
                booking,
                Booking.builder()
                        .id(2L)
                        .status(Status.APPROVED)
                        .start(LocalDateTime.of(2021, 1, 1, 1, 1, 1))
                        .finish(LocalDateTime.of(2021, 2, 1, 1, 1, 1))
                        .renter(user)
                        .item(Item.builder()
                                .id(2L)
                                .build())
                        .build()
        );
        when(mockService.findAllByRenterId(1L, Status.ALL)).thenReturn(bookings);

        mockMvc.perform(get("/bookings")
                        .header(USER_REQUEST_HEADER, 1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is(Status.WAITING.toString())))
                .andExpect(jsonPath("$[0].start",
                        is(LocalDateTime.of(2020, 1, 1, 1, 1, 1).toString())))
                .andExpect(jsonPath("$[0].end",
                        is(LocalDateTime.of(2020, 2, 1, 1, 1, 1).toString())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].status", is(Status.APPROVED.toString())))
                .andExpect(jsonPath("$[1].start",
                        is(LocalDateTime.of(2021, 1, 1, 1, 1, 1).toString())))
                .andExpect(jsonPath("$[1].end",
                        is(LocalDateTime.of(2021, 2, 1, 1, 1, 1).toString())));
        verify(mockService, times(1)).findAllByRenterId(1L, Status.ALL);
    }

    @Test
    @DisplayName("Request GET /bookings/1, expected host answer OK")
    void testGetBookingById_OK_200() throws Exception {
        mockMvc.perform(get("/bookings/1")
                        .header(USER_REQUEST_HEADER, 1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(Status.WAITING.toString())))
                .andExpect(jsonPath("$.start",
                        is(LocalDateTime.of(2020, 1, 1, 1, 1, 1).toString())))
                .andExpect(jsonPath("$.end",
                        is(LocalDateTime.of(2020, 2, 1, 1, 1, 1).toString())));
        verify(mockService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Request PATCH /bookings/1, expected host answer OK")
    void testUpdateBookingStatus_OK_200() throws Exception {
        booking.setStatus(Status.APPROVED);
        when(mockService.updateStatus(1L, true)).thenReturn(booking);

        mockMvc.perform(patch("/bookings/1")
                        .content(jsonMapper.writeValueAsString(booking))
                        .param("approved", String.valueOf(true))
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.status", is(Status.APPROVED.toString())))
                .andExpect(jsonPath("$.start",
                        is(LocalDateTime.of(2020, 1, 1, 1, 1, 1).toString())))
                .andExpect(jsonPath("$.end",
                        is(LocalDateTime.of(2020, 2, 1, 1, 1, 1).toString())));
        verify(mockService, times(1)).updateStatus(1L, true);
    }

    @Test
    @DisplayName("Request DELETE /bookings/1, expected host answer OK")
    void testDeleteBooking() throws Exception {
        doNothing().when(mockService).deleteById(1L, 1L);
        mockMvc.perform(delete("/bookings/1")
                        .header(USER_REQUEST_HEADER, 1L))
                .andExpect(status().isOk());
        verify(mockService, times(1)).deleteById(1L, 1L);
    }
}
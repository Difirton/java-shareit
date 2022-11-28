package shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.service.ItemManager;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.web.convertor.CommentDtoToCommentConverter;
import ru.practicum.shareit.item.web.convertor.CommentToCommentDtoConverter;
import ru.practicum.shareit.item.web.convertor.ItemDtoToItemConverter;
import ru.practicum.shareit.item.web.convertor.ItemToItemDtoConverter;
import ru.practicum.shareit.item.web.dto.CommentDto;
import ru.practicum.shareit.item.web.dto.ItemDto;
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.user.repository.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ItemManagerImplTest {
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ItemToItemDtoConverter itemToItemDtoConverter;
    @Autowired
    private ItemDtoToItemConverter itemDtoToItemConverter;
    @Autowired
    private CommentDtoToCommentConverter commentDtoToCommentConverter;
    @Autowired
    private CommentToCommentDtoConverter commentToCommentDtoConverter;

    private Item item;
    private ItemDto itemDto;
    @MockBean
    private BookingService mockBookingService;
    @MockBean
    private ItemService mockItemService;
    @MockBean
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1L)
                .name("test1")
                .description("test1Desc")
                .owner(User.builder()
                        .id(1L)
                        .build())
                .itemRequest(ItemRequest.builder()
                        .id(4L)
                        .build())
                .available(true)
                .build();
        itemDto = itemToItemDtoConverter.convert(item);
    }

    @Test
    @DisplayName("Create new item, expected OK")
    void testSave() {
        when(mockItemService.save(item)).thenReturn(item);
        itemManager.save(itemDto, 1L);
        verify(mockItemService, times(1)).save(item);
    }

    @Test
    @DisplayName("Test find all items, expected OK")
    void testFindAll() {
        when(mockItemService.findAll(1L)).thenReturn(List.of(item));
        List<ItemDto> dto = itemManager.findAll(1L);
        verify(mockItemService, times(1)).findAll(1L);
        assertEquals(List.of(itemDto), dto);
    }

    @Test
    @DisplayName("Test set bookings new item, expected OK")
    void testSetBookings() {
        when(mockBookingService.findNextBookingByItemIdAndStatuses(1L, List.of(Status.APPROVED)))
                .thenReturn(Optional.empty());
        when(mockBookingService.findLastBookingByItemIdAndStatuses(1L, List.of(Status.APPROVED)))
                .thenReturn(Optional.empty());
        itemManager.setBookings(itemDto);
        verify(mockBookingService, times(1)).findNextBookingByItemIdAndStatuses(1L,
                List.of(Status.APPROVED));
        verify(mockBookingService, times(1)).findLastBookingByItemIdAndStatuses(1L,
                List.of(Status.APPROVED));
    }

    @Test
    @DisplayName("Test set bookings new item, expected OK")
    void testSetBookings2() {
        when(mockBookingService.findNextBookingByItemIdAndStatuses(1L, List.of(Status.APPROVED)))
                .thenReturn(Optional.of(Booking.builder()
                        .id(2L)
                        .renter(User.builder()
                                .id(2L)
                                .build())
                        .status(Status.APPROVED)
                        .build()));
        when(mockBookingService.findLastBookingByItemIdAndStatuses(1L, List.of(Status.APPROVED)))
                .thenReturn(Optional.of(Booking.builder()
                        .id(4L)
                        .renter(User.builder()
                                        .id(3L)
                                        .build())
                        .status(Status.APPROVED)
                        .build()));
        ItemDto returnedItemDto = itemManager.setBookings(itemDto);
        verify(mockBookingService, times(1)).findNextBookingByItemIdAndStatuses(1L,
                List.of(Status.APPROVED));
        verify(mockBookingService, times(1)).findLastBookingByItemIdAndStatuses(1L,
                List.of(Status.APPROVED));
        assertEquals(4L, returnedItemDto.getLastBooking().getId());
        assertEquals(2L, returnedItemDto.getNextBooking().getId());
        assertEquals(3L, returnedItemDto.getLastBooking().getBookerId());
        assertEquals(2L, returnedItemDto.getNextBooking().getBookerId());
    }

    @Test
    @DisplayName("Test find by id, expected OK")
    void testFindById() {
        when(mockItemService.findById(1L)).thenReturn(item);
        itemManager.findById(1L, 1L);
        verify(mockItemService, times(2)).findById(1L);
    }

    @Test
    @DisplayName("Test find by id, expected OK")
    void testFindById2() {
        when(mockItemService.findById(1L)).thenReturn(item);
        itemManager.findById(1L, 2L);
        verify(mockItemService, times(2)).findById(1L);
    }

    @Test
    @DisplayName("Test update by id, expected OK")
    void testUpdate() {
        when(mockItemService.update(1L, item)).thenReturn(item);
        itemManager.update(1L, 2L, itemDto);
        verify(mockItemService, times(1)).update(1L, itemDtoToItemConverter.convert(itemDto));
    }

    @Test
    @DisplayName("Test delete by id, expected OK")
    void deleteById() {
        itemManager.deleteById(1L, 2L);
        verify(mockItemService, times(1)).deleteById(1L, 2L);
    }

    @Test
    @DisplayName("Test find by param, expected OK")
    void findItemByParam() {
        when(mockItemService.findByParam("test")).thenReturn(List.of(item));
        itemManager.findItemByParam("test");
        verify(mockItemService, times(1)).findByParam("test");
    }

    @Test
    @DisplayName("Test save comment, expected OK")
    void testSaveComment() {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("test")
                .itemId(1L)
                .authorId(1L)
                .authorName("Name")
                .build();
        when(mockItemService.saveComment(commentDtoToCommentConverter.convert(commentDto)))
                .thenReturn(commentDtoToCommentConverter.convert(commentDto));
        itemManager.saveComment(1L, 2L, commentDto);
        verify(mockItemService, times(1))
                .saveComment(commentDtoToCommentConverter.convert(commentDto));
    }
}
package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.error.ItemAuthenticationException;
import ru.practicum.shareit.item.error.ItemNotAvailableException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.item.repository.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ItemServiceImplTest {
    private Item item;

    @Autowired
    private ItemService itemService;

    @MockBean
    BookingRepository mockBookingRepository;

    @MockBean
    private ItemRepository mockRepository;

    @MockBean
    private UserRepository mockUserRepository;

    @MockBean
    private CommentRepository mockCommentRepository;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .id(1L)
                .build();
        item = Item.builder()
                .id(1L)
                .name("test")
                .description("testDesc")
                .available(true)
                .owner(user)
                .build();
        when(mockUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mockRepository.save(item)).thenReturn(item);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    @DisplayName("Create new item, expected OK")
    void testSave() {
        Item savedItem = itemService.save(item);
        assertEquals(1L, savedItem.getId());
        assertEquals("test", savedItem.getName());
        assertEquals("testDesc", savedItem.getDescription());
        assertEquals(true, savedItem.getAvailable());
        verify(mockRepository, times(1)).save(item);
    }

    @Test
    @DisplayName("Find all items, expected OK")
    void testFindAll() {
        List<Item> items = List.of(
                item,
                Item.builder()
                        .id(2L)
                        .name("test2")
                        .description("testDesc2")
                        .available(false)
                        .owner(User.builder()
                                .id(1L)
                                .build())
                        .build()
        );
        when(mockRepository.findAllByOwnerIdOrderById(1L)).thenReturn(items);

        List<Item> returnedItems = itemService.findAll(1L);
        assertEquals(item, returnedItems.get(0));
        assertEquals(2L, returnedItems.get(1).getId());
        assertEquals("test2", returnedItems.get(1).getName());
        assertEquals("testDesc2", returnedItems.get(1).getDescription());
        assertEquals(false, returnedItems.get(1).getAvailable());
        verify(mockRepository, times(1)).findAllByOwnerIdOrderById(1L);
    }

    @Test
    @DisplayName("Find item by Id, expected OK")
    void testFindById() {
        when(mockCommentRepository.findAllByItemId(1L)).thenReturn(List.of());
        Item returnedItem = itemService.findById(1L);
        assertEquals(1L, returnedItem.getId());
        assertEquals("test", returnedItem.getName());
        assertEquals("testDesc", returnedItem.getDescription());
        assertEquals(true, returnedItem.getAvailable());
        verify(mockRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find item by Id, expected Item Not Found Exception")
    void testFindByIdNotFound() {
        assertThrows(ItemNotFoundException.class, () -> itemService.findById(2L));
    }

    @Test
    @DisplayName("Update item by Id all fields, expected OK")
    void testUpdateAllFields() {
        Item updatedItem = Item.builder()
                .name("updated")
                .description("updatedDesc")
                .available(false)
                .owner(User.builder()
                        .id(1L)
                        .build())
                .build();
        Item returnedItem = itemService.update(1L, updatedItem);
        assertEquals(1L, returnedItem.getId());
        assertEquals("updated", returnedItem.getName());
        assertEquals("updatedDesc", returnedItem.getDescription());
        assertEquals(false, returnedItem.getAvailable());
        verify(mockRepository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("Update item by Id part fields, expected OK")
    void testUpdatePartFields() {
        Item updatedItem = Item.builder()
                .name("updated")
                .available(false)
                .owner(User.builder()
                        .id(1L)
                        .build())
                .build();
        Item returnedItem = itemService.update(1L, updatedItem);
        assertEquals(1L, returnedItem.getId());
        assertEquals("updated", returnedItem.getName());
        assertEquals("testDesc", returnedItem.getDescription());
        assertEquals(false, returnedItem.getAvailable());
        verify(mockRepository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("Update item by Id, expected Item Not Found Exception")
    void testUpdateByIdNotFound() {
        Item updatedItem = Item.builder()
                .name("updated")
                .available(false)
                .owner(User.builder()
                        .id(1L)
                        .build())
                .build();
        assertThrows(ItemNotFoundException.class, () -> itemService.update(2L, updatedItem));
    }

    @Test
    @DisplayName("Update item by Id with wrong owner Id, expected Item Authentication Exception")
    void testUpdateByIdAuthExc() {
        Item updatedItem = Item.builder()
                .name("updated")
                .available(false)
                .owner(User.builder()
                        .id(2L)
                        .build())
                .build();
        assertThrows(ItemAuthenticationException.class, () -> itemService.update(1L, updatedItem));
    }

    @Test
    @DisplayName("Delete item by Id, expected OK")
    void testDeleteById() {
        itemService.deleteById(1L, 1L);
        verify(mockRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete item by Id, expected Item Authentication Exception")
    void testDeleteByIdAuthExc() {
        item.setOwner(User.builder()
                .id(2L)
                .build());
        assertThrows(ItemAuthenticationException.class, () -> itemService.deleteById(1L, 1L));
    }

    @Test
    @DisplayName("Delete item by Id, expected Item Not Found Exception")
    void testDeleteByIdNotFound() {
        assertThrows(ItemNotFoundException.class, () -> itemService.deleteById(2L, 1L));
    }

    @Test
    @DisplayName("Find by param, expected OK")
    void testFindByParam() {
        itemService.findByParam("test");
        verify(mockRepository, times(1)).findAllByCriteria("test");
    }

    @Test
    @DisplayName("Find item by id if available, expected OK")
    void testFindAvailableById() {
        Item returnedItem = itemService.findAvailableById(1L);
        assertEquals(1L, returnedItem.getId());
        assertEquals("test", returnedItem.getName());
        assertEquals("testDesc", returnedItem.getDescription());
        assertEquals(true, returnedItem.getAvailable());
        verify(mockRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find item by id if available, expected Item Not Found Exception")
    void findFirstByItemIdAndRenterIdAndStatusAndFinishBeforeFindAvailableNotFound() {
        assertThrows(ItemNotFoundException.class, () -> itemService.findAvailableById(2L));
    }

    @Test
    @DisplayName("Find item by id if available, expected Item Not Available Exception")
    void testFindAvailableNotAvailable() {
        item.setAvailable(false);
        assertThrows(ItemNotAvailableException.class, () -> itemService.findAvailableById(1L));
    }

    @Test
    @DisplayName("Find available renter, expected OK")
    void testFindAvailableRenter() {
        when(mockRepository.findItemByIdWithCheckNotOwner(1L, 1L)).thenReturn(Optional.of(item));
        itemService.findAvailableRenter(1L, 1L);
        verify(mockRepository, times(1)).findItemByIdWithCheckNotOwner(1L, 1L);
    }

    @Test
    @DisplayName("Find available renter, expected Illegal State Exception")
    void testFindAvailableRenter1() {
        assertThrows(IllegalStateException.class, () -> itemService.findAvailableRenter(1L, 1L));
    }

    @Test
    @DisplayName("Find available renter, expected Item Not Available Exception")
    void testFindAvailableRenter2() {
        item.setAvailable(false);
        when(mockRepository.findItemByIdWithCheckNotOwner(1L, 1L)).thenReturn(Optional.of(item));
        assertThrows(ItemNotAvailableException.class, () -> itemService.findAvailableRenter(1L, 1L));
    }

    @Test
    @DisplayName("Test save comment, expected OK")
    void testSaveComment() {
        when(mockBookingRepository.findFirstByItemIdAndRenterIdAndStatusAndFinishBefore(anyLong(), anyLong(),
                any(Status.class), any(LocalDateTime.class))).thenReturn(Optional.of(Booking.builder()
                                                                        .status(Status.APPROVED)
                                                                        .renter(User.builder()
                                                                        .id(4L)
                                                                        .build())
                                                                        .item(item)
                                                                        .build()));
        Comment comment = Comment.builder()
                .item(item)
                .author(item.getOwner())
                .text("test")
                .build();
        itemService.saveComment(comment);
        verify(mockCommentRepository, times(1)).save(comment);

    }

    @Test
    @DisplayName("Test find all by item id, expected OK")
    void testFindAllByItemId() {
        when(mockBookingRepository.findAllByItemIdOrderByStart(1L)).thenReturn(new ArrayList<>());
        itemService.findAllByItemId(1L);
    }
}
package shareit.item_request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.item_request.error.ItemRequestAuthenticationException;
import ru.practicum.shareit.item_request.error.ItemRequestNotFoundException;
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.item_request.repository.ItemRequestRepository;
import ru.practicum.shareit.item_request.service.ItemRequestService;
import ru.practicum.shareit.user.repository.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ItemRequestServiceImplTest {
    ItemRequest itemRequest;

    @Autowired
    ItemRequestService itemRequestService;

    @MockBean
    UserService mockUserService;
    @MockBean
    ItemRequestRepository mockRepository;

    @BeforeEach
    void setUp() {
        itemRequest = ItemRequest.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.of(2020, 1, 1, 1, 1, 1))
                .user(User.builder()
                        .id(1L)
                        .build())
                .build();
        when(mockRepository.save(itemRequest)).thenReturn(itemRequest);
        when(mockRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
    }

    @Test
    @DisplayName("Create new item request, expected OK")
    void testSave() {
        ItemRequest savedItemRequest = itemRequestService.save(itemRequest);
        assertEquals(1L, savedItemRequest.getId());
        assertEquals("test", savedItemRequest.getDescription());
        assertEquals(LocalDateTime.now().withNano(0),
                savedItemRequest.getCreated().withNano(0));
        verify(mockRepository, times(1)).save(savedItemRequest);
    }

    @Test
    @DisplayName("Find all items requests, expected OK")
    void testFindAll() {
        List<ItemRequest> itemsRequests = List.of(
                itemRequest,
                ItemRequest.builder()
                        .id(2L)
                        .description("test2")
                        .created(LocalDateTime.of(2020, 2, 1, 1, 1, 1))
                        .user(User.builder()
                                .id(1L)
                                .build())
                        .build()
        );
        when(mockRepository.findAllByUserIdOrderByCreatedDesc(1L)).thenReturn(itemsRequests);

        List<ItemRequest> returnedItemsRequests = itemRequestService.findAll(1L);
        assertEquals(itemRequest, returnedItemsRequests.get(0));
        assertEquals(2L, returnedItemsRequests.get(1).getId());
        assertEquals("test2", returnedItemsRequests.get(1).getDescription());
        assertEquals(LocalDateTime.of(2020, 2, 1, 1, 1, 1),
                returnedItemsRequests.get(1).getCreated());
        verify(mockRepository, times(1)).findAllByUserIdOrderByCreatedDesc(1L);
    }

    @Test
    @DisplayName("Find item request by Id, expected OK")
    void testFindById() {
        ItemRequest returnedItemRequest = itemRequestService.findById(1L, 1L);
        assertEquals(1L, returnedItemRequest.getId());
        assertEquals("test", returnedItemRequest.getDescription());
        assertEquals(LocalDateTime.of(2020, 1, 1, 1, 1, 1),
                returnedItemRequest.getCreated());
        verify(mockRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Find item request by Id, expected Item Request Not Found Exception")
    void testFindByIdNotFound() {
        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.findById(2L, 1L));
    }

    @Test
    @DisplayName("Update item request by Id all fields, expected OK")
    void testUpdateAllFields() {
        ItemRequest updatedItemRequest = ItemRequest.builder()
                .description("updated")
                .created(LocalDateTime.of(2020, 2, 1, 1, 1, 1))
                .user(User.builder()
                        .id(1L)
                        .build())
                .build();
        ItemRequest returnedRequest = itemRequestService.update(1L, updatedItemRequest);
        assertEquals(1L, returnedRequest.getId());
        assertEquals("updated", returnedRequest.getDescription());
        assertEquals(LocalDateTime.of(2020, 2, 1, 1, 1, 1),
                returnedRequest.getCreated());
        verify(mockRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    @DisplayName("Update item request by Id part fields, expected OK")
    void testUpdatePartFields() {
        ItemRequest updatedItemRequest = ItemRequest.builder()
                .description("updated")
                .user(User.builder()
                        .id(1L)
                        .build())
                .build();
        ItemRequest returnedRequest = itemRequestService.update(1L, updatedItemRequest);
        assertEquals(1L, returnedRequest.getId());
        assertEquals("updated", returnedRequest.getDescription());
        assertEquals(LocalDateTime.of(2020, 1, 1, 1, 1, 1),
                returnedRequest.getCreated());
        verify(mockRepository, times(1)).save(any(ItemRequest.class));
    }

    @Test
    @DisplayName("Update item request by Id, expected Item Not Found Exception")
    void testUpdateByIdNotFound() {
        ItemRequest updatedItemRequest = ItemRequest.builder()
                .description("updated")
                .user(User.builder()
                        .id(1L)
                        .build())
                .build();
        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.update(2L, updatedItemRequest));
    }

    @Test
    @DisplayName("Update item request by Id with wrong owner Id, expected Item Authentication Exception")
    void testUpdateByIdAuthExc() {
        ItemRequest updatedItemRequest = ItemRequest.builder()
                .description("updated")
                .user(User.builder()
                        .id(2L)
                        .build())
                .build();
        assertThrows(ItemRequestAuthenticationException.class, () -> itemRequestService.update(1L, updatedItemRequest));
    }

    @Test
    @DisplayName("Delete item request by Id, expected OK")
    void testDeleteById() {
        itemRequestService.deleteById(1L, 1L);
        verify(mockRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete item request by Id, expected Item Authentication Exception")
    void testDeleteByIdAuthExc() {
        itemRequest.setUser(User.builder()
                .id(2L)
                .build());
        assertThrows(ItemRequestAuthenticationException.class, () -> itemRequestService.deleteById(1L, 1L));
    }

    @Test
    @DisplayName("Delete item request by Id, expected Item Not Found Exception")
    void testDeleteByIdNotFound() {
        assertThrows(ItemRequestNotFoundException.class, () -> itemRequestService.deleteById(2L, 1L));
    }

}
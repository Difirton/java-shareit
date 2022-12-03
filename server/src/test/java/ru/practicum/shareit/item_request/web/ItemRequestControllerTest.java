package ru.practicum.shareit.item_request.web;

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
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.item_request.service.ItemRequestService;
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
class ItemRequestControllerTest {
    private static final ObjectMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();
    private ItemRequest itemRequest;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemRequestService mockService;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

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
        when(mockService.findById(1L, 1L)).thenReturn(itemRequest);
    }

    @Test
    @DisplayName("Request POST /requests, expected host answer CREATED")
    void testCreateItemRequest_CREATED_201() throws Exception {
        ItemRequest newItemRequest = ItemRequest.builder()
                .id(2L)
                .description("test2")
                .created(LocalDateTime.of(2020, 2, 1, 1, 1, 1))
                .user(User.builder()
                        .id(1L)
                        .build())
                .build();
        when(mockService.save(any(ItemRequest.class))).thenReturn(newItemRequest);

        mockMvc.perform(post("/requests")
                        .content(jsonMapper.writeValueAsString(newItemRequest))
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.description", is("test2")))
                .andExpect(jsonPath("$.created",
                        is(LocalDateTime.of(2020, 2, 1, 1, 1, 1).toString())));
        verify(mockService, times(1)).save(any(ItemRequest.class));
    }

    @Test
    @DisplayName("Request GET /requests, expected host answer OK")
    void testGetItemsRequests_OK_200() throws Exception {
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
        when(mockService.findAll(1L)).thenReturn(itemsRequests);

        mockMvc.perform(get("/requests")
                        .header(USER_REQUEST_HEADER, 1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("test")))
                .andExpect(jsonPath("$[0].created",
                        is(LocalDateTime.of(2020, 1, 1, 1, 1, 1).toString())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].description", is("test2")))
                .andExpect(jsonPath("$[1].created",
                        is(LocalDateTime.of(2020, 2, 1, 1, 1, 1).toString())));
        verify(mockService, times(1)).findAll(1L);
    }

    @Test
    @DisplayName("Request GET /requests/1, expected host answer OK")
    void testGetItemRequestById_OK_200() throws Exception {
        mockMvc.perform(get("/requests/1")
                        .header(USER_REQUEST_HEADER, 1L)
                        .content(jsonMapper.writeValueAsString(itemRequest))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("test")))
                .andExpect(jsonPath("$.created",
                        is(LocalDateTime.of(2020, 1, 1, 1, 1, 1).toString())));
        verify(mockService, times(1)).findById(1L, 1L);
    }

    @Test
    @DisplayName("Request PATCH /requests/1, expected host answer OK")
    void testUpdateItemRequestById_OK_200() throws Exception {
        ItemRequest updatedItemRequest = ItemRequest.builder()
                .id(1L)
                .description("updated")
                .created(LocalDateTime.of(2020, 2, 1, 1, 1, 1))
                .user(User.builder()
                        .id(1L)
                        .build())
                .build();
        when(mockService.update(1L, itemRequest)).thenReturn(updatedItemRequest);

        mockMvc.perform(patch("/requests/1")
                        .content(jsonMapper.writeValueAsString(itemRequest))
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("updated")))
                .andExpect(jsonPath("$.created",
                        is(LocalDateTime.of(2020, 2, 1, 1, 1, 1).toString())));
        verify(mockService, times(1)).update(1L, itemRequest);
    }

    @Test
    @DisplayName("Request DELETE /items/1, expected host answer OK")
    void testDeleteItemRequest() throws Exception {
        doNothing().when(mockService).deleteById(1L, 1L);
        mockMvc.perform(delete("/requests/1")
                        .header(USER_REQUEST_HEADER, 1L))
                .andExpect(status().isOk());
        verify(mockService, times(1)).deleteById(1L, 1L);
    }

    @Test
    @DisplayName("Request GET /requests, expected host answer OK")
    void testGetPageableItemsRequests() throws Exception {
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
        when(mockService.findAllPageable(1L, 0, 20)).thenReturn(itemsRequests);

        mockMvc.perform(get("/requests/all")
                        .header(USER_REQUEST_HEADER, 1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].description", is("test")))
                .andExpect(jsonPath("$[0].created",
                        is(LocalDateTime.of(2020, 1, 1, 1, 1, 1).toString())))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].description", is("test2")))
                .andExpect(jsonPath("$[1].created",
                        is(LocalDateTime.of(2020, 2, 1, 1, 1, 1).toString())));
        verify(mockService, times(1)).findAllPageable(1L, 0, 20);
    }
}
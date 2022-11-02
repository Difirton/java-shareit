package ru.practicum.shareit.item.web;

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
import ru.practicum.shareit.item.repository.Comment;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.web.convertor.CommentToCommentDtoConverter;
import ru.practicum.shareit.item.web.dto.ItemDto;
import ru.practicum.shareit.user.repository.User;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ItemControllerTest {
    private static final ObjectMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();
    private Item item;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ItemService mockService;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        item = Item.builder()
                .id(1L)
                .name("test1")
                .description("test1Desc")
                .owner(User.builder()
                        .id(1L)
                        .build())
                .available(true)
                .build();
        when(mockService.findById(1L)).thenReturn(item);
    }

    @Test
    @DisplayName("Request POST /items, expected host answer CREATED")
    void testCreateItem_CREATED_201() throws Exception {
        Item newItem = Item.builder()
                .id(2L)
                .name("test2")
                .description("test2Desc")
                .available(false)
                .build();
        when(mockService.save(any(Item.class))).thenReturn(newItem);

        mockMvc.perform(post("/items")
                        .content(jsonMapper.writeValueAsString(newItem))
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("test2")))
                .andExpect(jsonPath("$.description", is("test2Desc")))
                .andExpect(jsonPath("$.available", is(false)));
        verify(mockService, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("Request GET /items, expected host answer OK")
    void testGetItems_OK_200() throws Exception {
        List<Item> items = Arrays.asList(
                item,
                Item.builder()
                        .id(2L)
                        .name("test2")
                        .description("test2Desc")
                        .available(false)
                        .build());
        when(mockService.findAll(1L)).thenReturn(items);

        mockMvc.perform(get("/items")
                        .header(USER_REQUEST_HEADER, 1L))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("test1")))
                .andExpect(jsonPath("$[0].description", is("test1Desc")))
                .andExpect(jsonPath("$[0].available", is(true)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("test2")))
                .andExpect(jsonPath("$[1].description", is("test2Desc")))
                .andExpect(jsonPath("$[1].available", is(false)));
        verify(mockService, times(1)).findAll(1L);
    }

    @Test
    @DisplayName("Request GET /items/1, expected host answer OK")
    void testGetItemById_OK_200() throws Exception {
        mockMvc.perform(get("/items/1")
                        .content(jsonMapper.writeValueAsString(item))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("test1")))
                .andExpect(jsonPath("$.description", is("test1Desc")))
                .andExpect(jsonPath("$.available", is(true)));
        verify(mockService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Request PATCH /items/1, expected host answer OK")
    void testUpdateItemById_OK_200() throws Exception {
        Item updatedItem = Item.builder()
                .id(1L)
                .name("updated")
                .description("test2Desc")
                .owner(User.builder()
                        .id(1L)
                        .build())
                .available(false)
                .build();
        when(mockService.update(1L, item)).thenReturn(updatedItem);

        mockMvc.perform(patch("/items/1")
                        .content(jsonMapper.writeValueAsString(item))
                        .header(USER_REQUEST_HEADER, 1L)
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("updated")))
                .andExpect(jsonPath("$.description", is("test2Desc")))
                .andExpect(jsonPath("$.available", is(false)));
        verify(mockService, times(1)).update(1L, item);
    }

    @Test
    @DisplayName("Request DELETE /items/1, expected host answer OK")
    void testDeleteItem_OK_200() throws Exception {
        doNothing().when(mockService).deleteById(1L, 1L);
        mockMvc.perform(delete("/items/1")
                        .header(USER_REQUEST_HEADER, 1L))
                .andExpect(status().isOk());
        verify(mockService, times(1)).deleteById(1L, 1L);
    }

    @Test
    @DisplayName("Request GET /items/search, expected host answer OK")
    void testGetItemByNameAndDescription_OK_200() throws Exception {
        List<Item> items = List.of(item);

        when(mockService.findByParam("param")).thenReturn(items);
        mockMvc.perform(get("/items/search")
                        .param("text", "param"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("test1")))
                .andExpect(jsonPath("$[0].description", is("test1Desc")))
                .andExpect(jsonPath("$[0].available", is(true)));
        verify(mockService, times(1)).findByParam("param");
    }
}
package ru.practicum.shareit.item_request.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:schema.sql", "classpath:sql_scripts/data_findItemRequestsWithGraph.sql"},
        executionPhase = BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:sql_scripts/drop_tables.sql", executionPhase = AFTER_TEST_METHOD)
class ItemRequestCustomRepositoryImplTest {
    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Test
    @DisplayName("Test entity graph in method: findWithItemAll")
    void testFindWithItemAll() {
        List<ItemRequest> itemRequests = itemRequestRepository.findWithItemAll();
        assertEquals(1L, itemRequests.get(0).getId());
        assertEquals(2, itemRequests.get(0).getItems().size());
        assertEquals(3, itemRequests.get(1).getItems().size());
        assertEquals(1, itemRequests.get(2).getItems().size());
        assertEquals("test2ff", itemRequests.get(0).getItems().get(1).getName());
        assertEquals("test4", itemRequests.get(1).getItems().get(1).getName());
        assertEquals("test6te", itemRequests.get(2).getItems().get(0).getName());
    }

    @Test
    @DisplayName("Test entity graph in method: testFindWithItemById")
    void testFindWithItemById() {
        ItemRequest itemRequest = itemRequestRepository.findWithItemById(2L);
        assertEquals(3, itemRequest.getItems().size());
        assertEquals("test3ff", itemRequest.getItems().get(0).getName());
        assertEquals("desc3Test", itemRequest.getItems().get(0).getDescription());
        assertEquals("test4", itemRequest.getItems().get(1).getName());
        assertEquals("desc4Testff", itemRequest.getItems().get(1).getDescription());
    }
}
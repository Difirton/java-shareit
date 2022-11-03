package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"classpath:sql_scripts/data_findAllByCriteria.sql"},
        executionPhase = BEFORE_TEST_METHOD)
class ItemCustomRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Test query: find all items by available")
    void testFindAllByCriteriaAvailable() {
        List<Item> items = itemRepository.findAllByCriteria("test");
        assertEquals(1L, items.get(0).getId());
        assertEquals("testte", items.get(0).getName());
        assertEquals("descTestss", items.get(0).getDescription());
        assertEquals(3L, items.get(1).getId());
        assertEquals("test3ff", items.get(1).getName());
        assertEquals("desc3Test", items.get(1).getDescription());
        assertEquals(4L, items.get(2).getId());
        assertEquals("test4", items.get(2).getName());
        assertEquals("desc4Testff", items.get(2).getDescription());
        assertEquals(6L, items.get(3).getId());
        assertEquals("test6te", items.get(3).getName());
        assertEquals("desc6ss", items.get(3).getDescription());
    }

    @Test
    @DisplayName("Test query: find all items by text in name or description")
    void testFindAllByCriteriaNameAndDescription() {
        List<Item> items = itemRepository.findAllByCriteria("ff");
        assertEquals(3L, items.get(0).getId());
        assertEquals("test3ff", items.get(0).getName());
        assertEquals("desc3Test", items.get(0).getDescription());
        assertEquals(4L, items.get(1).getId());
        assertEquals("test4", items.get(1).getName());
        assertEquals("desc4Testff", items.get(1).getDescription());
    }

    @Test
    @DisplayName("Test query: find all items by text in description")
    void testFindAllByCriteriaOnlyDescription() {
        List<Item> items = itemRepository.findAllByCriteria("ss");
        assertEquals(1L, items.get(0).getId());
        assertEquals("testte", items.get(0).getName());
        assertEquals("descTestss", items.get(0).getDescription());
        assertEquals(6L, items.get(1).getId());
        assertEquals("test6te", items.get(1).getName());
        assertEquals("desc6ss", items.get(1).getDescription());
    }

    @Test
    @DisplayName("Test query: find all items by text in name")
    void testFindAllByCriteriaOnlyName() {
        List<Item> items = itemRepository.findAllByCriteria("4");
        assertEquals(4L, items.get(0).getId());
        assertEquals("test4", items.get(0).getName());
        assertEquals("desc4Testff", items.get(0).getDescription());
    }
}
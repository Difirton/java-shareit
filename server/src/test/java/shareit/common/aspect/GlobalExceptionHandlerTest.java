package shareit.common.aspect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.common.aspect.GlobalExceptionHandler;
import ru.practicum.shareit.item.error.ItemAuthenticationException;
import ru.practicum.shareit.item.error.ItemNotAvailableException;
import ru.practicum.shareit.item.error.ItemNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GlobalExceptionHandlerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    @DisplayName("Test handle not found")
    void handleNotFound() {
        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleNotFound(new ItemNotFoundException(1L),
                null);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test handle bad request")
    void handleBadRequest() {
        ResponseEntity<Object> responseEntity = globalExceptionHandler
                .handleBadRequest(new ItemNotAvailableException(1L), null);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("Test handle forbidden")
    void handleForbidden() {
        ResponseEntity<Object> responseEntity = globalExceptionHandler
                .handleForbidden(new ItemAuthenticationException(1L, 1L), null);
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }
}
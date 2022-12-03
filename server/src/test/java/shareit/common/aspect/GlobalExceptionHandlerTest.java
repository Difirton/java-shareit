package shareit.common.aspect;

//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest
//class GlobalExceptionHandlerTest {
//    @Autowired
//    private GlobalExceptionHandler globalExceptionHandler;
//
//    @Test
//    @DisplayName("Test handle not found")
//    void handleNotFound() {
//        ResponseEntity<Object> responseEntity = globalExceptionHandler.handleNotFound(new ItemNotFoundException(1L),
//                null);
//        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test handle bad request")
//    void handleBadRequest() {
//        ResponseEntity<Object> responseEntity = globalExceptionHandler
//                .handleBadRequest(new ItemNotAvailableException(1L), null);
//        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("Test handle forbidden")
//    void handleForbidden() {
//        ResponseEntity<Object> responseEntity = globalExceptionHandler
//                .handleForbidden(new ItemAuthenticationException(1L, 1L), null);
//        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
//    }
//}
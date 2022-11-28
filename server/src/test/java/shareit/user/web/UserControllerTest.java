package shareit.user.web;

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
import ru.practicum.shareit.user.repository.User;
import ru.practicum.shareit.user.service.UserService;

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
class UserControllerTest {
    private static final ObjectMapper jsonMapper = JsonMapper.builder().findAndAddModules().build();
    private User user;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService mockService;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("test1")
                .email("test1@test1.ru")
                .build();
        when(mockService.findById(1L)).thenReturn(user);
    }

    @Test
    @DisplayName("Request POST /users, expected host answer CREATED")
    void testCreateUser_CREATED_201() throws Exception {
        User newUser = User.builder()
                .id(2L)
                .name("test2")
                .email("test2@test2.ru")
                .build();
        when(mockService.save(any(User.class))).thenReturn(newUser);

        mockMvc.perform(post("/users")
                        .content(jsonMapper.writeValueAsString(newUser))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.name", is("test2")))
                .andExpect(jsonPath("$.email", is("test2@test2.ru")));
        verify(mockService, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Request GET /users, expected host answer OK")
    void testGetUsers_OK_200() throws Exception {
        List<User> users = Arrays.asList(
                user,
                User.builder()
                        .id(2L)
                        .name("test2")
                        .email("test2@test2.ru")
                        .build());

        when(mockService.findAll()).thenReturn(users);
        mockMvc.perform(get("/users"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("test1")))
                .andExpect(jsonPath("$[0].email", is("test1@test1.ru")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("test2")))
                .andExpect(jsonPath("$[1].email", is("test2@test2.ru")));
        verify(mockService, times(1)).findAll();
    }

    @Test
    @DisplayName("Request GET /users/1, expected host answer OK")
    void testGetUserById_OK_200() throws Exception {
        mockMvc.perform(get("/users/1")
                        .content(jsonMapper.writeValueAsString(user))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("test1")))
                .andExpect(jsonPath("$.email", is("test1@test1.ru")));
        verify(mockService, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Request PATCH /users/1, expected host answer OK")
    void testUpdateUserById_OK_200() throws Exception {
        User updateUser = User.builder()
                .id(1L)
                .name("updated")
                .email("updated@updated.com")
                .build();
        when(mockService.update(1L, user)).thenReturn(updateUser);

        mockMvc.perform(patch("/users/1")
                        .content(jsonMapper.writeValueAsString(user))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("updated")))
                .andExpect(jsonPath("$.email", is("updated@updated.com")));
        verify(mockService, times(1)).update(1L, user);
    }

    @Test
    @DisplayName("Request DELETE /users/1, expected host answer OK")
    void testDeleteUser() throws Exception {
        doNothing().when(mockService).deleteById(1L);
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
        verify(mockService, times(1)).deleteById(1L);
    }
}
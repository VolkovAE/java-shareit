package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserDBServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.StringConstantsForRequest.REQUEST_MAPPING_PATH_USER;

@Slf4j
@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerServerTests {
//    static final int LENGTH_NAME_NEW_USER = 10;
//    static final int LENGTH_EMAIL_NEW_USER = 15;
//    static final String NAME_DOMAIN = "yandex.ru";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserDBServiceImpl userService;

    private static NewUserRequest newUserRequest;
    private static UserDto userDto;
    private static UpdateUserRequest updateUserRequest;

    @BeforeAll
    public static void init() {
        String name = "Alex";
        String email = "va@ya.ru";

        newUserRequest = new NewUserRequest();
        newUserRequest.setName(name);
        newUserRequest.setEmail(email);

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName(name);
        userDto.setEmail(email);

        updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName(name);
        updateUserRequest.setEmail(email);
    }

    @Test
    public void add() throws Exception {
        when(userService.add(any()))
                .thenReturn(userDto);

        mvc.perform(post(REQUEST_MAPPING_PATH_USER)
                        .content(mapper.writeValueAsString(newUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class));
    }

    @Test
    public void getById() throws Exception {
        when(userService.getById(any()))
                .thenReturn(userDto);

        mvc.perform(get(REQUEST_MAPPING_PATH_USER + "/1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class));
    }

    @Test
    public void findAll() throws Exception {
        when(userService.findAll(anyInt(), anyInt()))
                .thenReturn(List.of(userDto));

        mvc.perform(get(REQUEST_MAPPING_PATH_USER + "?page=0&count=5")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(userDto.getId()), Long.class));
    }

    @Test
    public void update() throws Exception {
        when(userService.update(anyLong(), any(UpdateUserRequest.class)))
                .thenReturn(userDto);

        mvc.perform(patch(REQUEST_MAPPING_PATH_USER + "/1")
                        .content(mapper.writeValueAsString(updateUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(userDto.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto.getEmail()), String.class));
    }

    @Test
    public void deleteTest() throws Exception {
        when(userService.delete(anyLong()))
                .thenReturn(userDto);

        mvc.perform(delete(REQUEST_MAPPING_PATH_USER + "/1")
                        .content(mapper.writeValueAsString(""))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId()), Long.class));
    }
}

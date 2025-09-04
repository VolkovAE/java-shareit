package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserControllerGateway;
import ru.practicum.shareit.user.dto.NewUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.service.UserClient;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.StringConstantsForRequest.REQUEST_MAPPING_PATH_USER;

/**
 * Цель этого модуля, проверить валидацию.
 * Важен статус ответа, а не тело. Тело проверяем при тестировании эндпоинтов модуля server.
 */

@Slf4j
@WebMvcTest(controllers = UserControllerGateway.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerGatewayTests {
    static final int LENGTH_NAME_NEW_USER = 10;
    static final int LENGTH_EMAIL_NEW_USER = 15;
    static final String NAME_DOMAIN = "yandex.ru";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserClient userClient;

    @Test
    public void add() throws Exception {
        when(userClient.add(any()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        NewUserRequest newUserRequest = getNewUserRequest();

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(post(REQUEST_MAPPING_PATH_USER)
                        .content(mapper.writeValueAsString(newUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных - email.
        newUserRequest.setEmail("154.ru");

        mvc.perform(post(REQUEST_MAPPING_PATH_USER)
                        .content(mapper.writeValueAsString(newUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].fieldName", is("email"), String.class))
                .andExpect(jsonPath("$.violations[0].message", is("Email is not valid"), String.class))
                .andExpect(jsonPath("$.error", is(true), Boolean.class));

        // Проверим, как пройдет валидация НЕ корректных данных - пустой email.
        newUserRequest.setEmail("");

        mvc.perform(post(REQUEST_MAPPING_PATH_USER)
                        .content(mapper.writeValueAsString(newUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].fieldName", is("add.userRequest.email"), String.class))
                .andExpect(jsonPath("$.violations[0].message", is("Электронная почта не может быть пустой."), String.class))
                .andExpect(jsonPath("$.error", is(true), Boolean.class));
    }

    private NewUserRequest getNewUserRequest() {
        String name = RandomStringUtils.randomAlphabetic(LENGTH_NAME_NEW_USER);

        StringBuilder builderEmail = new StringBuilder();
        builderEmail.append(RandomStringUtils.randomAlphabetic(LENGTH_EMAIL_NEW_USER));
        builderEmail.append("@");
        builderEmail.append(NAME_DOMAIN);

        NewUserRequest userRequest = new NewUserRequest();
        userRequest.setName(name);
        userRequest.setEmail(builderEmail.toString());

        return userRequest;
    }

    private UpdateUserRequest getUpdateUserRequest() {
        NewUserRequest newUserRequest = getNewUserRequest();

        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName(newUserRequest.getName());
        updateUserRequest.setEmail(newUserRequest.getEmail());

        return updateUserRequest;
    }

    @Test
    public void getById() throws Exception {
        when(userClient.getById(any()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(get(REQUEST_MAPPING_PATH_USER + "/1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных, например id user = 0.
        mvc.perform(get(REQUEST_MAPPING_PATH_USER + "/0")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.violations[0].fieldName", is("findById.userId"), String.class))
                .andExpect(jsonPath("$.violations[0].message", is("must be greater than 0"), String.class))
                .andExpect(jsonPath("$.error", is(true), Boolean.class));
    }

    @Test
    public void findAll() throws Exception {
        when(userClient.findAll(anyInt(), anyInt()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(get(REQUEST_MAPPING_PATH_USER + "?page=0&count=5")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных, например page < 0.
        mvc.perform(get(REQUEST_MAPPING_PATH_USER + "?page=-1&count=5")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Проверим, как пройдет валидация НЕ корректных данных, например count = 0.
        mvc.perform(get(REQUEST_MAPPING_PATH_USER + "?page=0&count=0")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update() throws Exception {
        when(userClient.update(anyLong(), any(UpdateUserRequest.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        UpdateUserRequest updateUserRequest = getUpdateUserRequest();

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(patch(REQUEST_MAPPING_PATH_USER + "/1")
                        .content(mapper.writeValueAsString(updateUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных, например id = 0.
        mvc.perform(patch(REQUEST_MAPPING_PATH_USER + "/0")
                        .content(mapper.writeValueAsString(updateUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Проверим, как пройдет валидация НЕ корректных данных, например формат имэйл.
        updateUserRequest.setEmail("123.ru");
        mvc.perform(patch(REQUEST_MAPPING_PATH_USER + "/1")
                        .content(mapper.writeValueAsString(updateUserRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteTest() throws Exception {
        when(userClient.delete(anyLong()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(delete(REQUEST_MAPPING_PATH_USER + "/1")
                        .content(mapper.writeValueAsString(""))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных, например id = 0.
        mvc.perform(delete(REQUEST_MAPPING_PATH_USER + "/0")
                        .content(mapper.writeValueAsString(""))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

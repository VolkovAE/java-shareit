package ru.practicum.shareit;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequestControllerGateway;
import ru.practicum.shareit.request.dto.NewRequestItem;
import ru.practicum.shareit.request.service.RequestClient;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.StringConstantsForRequest.NAME_HEADER_USER_ID;
import static ru.practicum.shareit.util.StringConstantsForRequest.REQUEST_MAPPING_PATH_REQUEST;

/**
 * Цель этого модуля, проверить валидацию.
 * Важен статус ответа, а не тело. Тело проверяем при тестировании эндпоинтов модуля server.
 */

@Slf4j
@WebMvcTest(controllers = ItemRequestControllerGateway.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestControllerGatewayTests {
    static final int LENGTH_NAME_NEW_USER = 10;
    static final int LENGTH_EMAIL_NEW_USER = 15;
    static final String NAME_DOMAIN = "yandex.ru";

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    RequestClient requestClient;

    @Test
    public void add() throws Exception {
        when(requestClient.add(any(NewRequestItem.class), anyLong()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        NewRequestItem newRequestItem = new NewRequestItem();
        newRequestItem.setDescription("12345");

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(post(REQUEST_MAPPING_PATH_REQUEST)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(newRequestItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных - user id = 0.
        mvc.perform(post(REQUEST_MAPPING_PATH_REQUEST)
                        .header(NAME_HEADER_USER_ID, "0")
                        .content(mapper.writeValueAsString(newRequestItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Проверим, как пройдет валидация НЕ корректных данных - пустой запрос.
        newRequestItem.setDescription("");

        mvc.perform(post(REQUEST_MAPPING_PATH_REQUEST)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(newRequestItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findAllMy() throws Exception {
        when(requestClient.findAllMy(anyLong()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(get(REQUEST_MAPPING_PATH_REQUEST)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных, например id user = 0.
        mvc.perform(get(REQUEST_MAPPING_PATH_REQUEST)
                        .header(NAME_HEADER_USER_ID, "0")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findAllOther() throws Exception {
        when(requestClient.findAllOther(anyLong()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(get(REQUEST_MAPPING_PATH_REQUEST + "/all")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных, например id user = 0.
        mvc.perform(get(REQUEST_MAPPING_PATH_REQUEST + "/all")
                        .header(NAME_HEADER_USER_ID, "0")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findById() throws Exception {
        when(requestClient.findById(anyLong()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(get(REQUEST_MAPPING_PATH_REQUEST + "/1")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных, например id request = 0.
        mvc.perform(get(REQUEST_MAPPING_PATH_REQUEST + "/0")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

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
import ru.practicum.shareit.item.ItemControllerGateway;
import ru.practicum.shareit.item.dto.NewCommentRequest;
import ru.practicum.shareit.item.dto.NewItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemClient;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.StringConstantsForRequest.*;

/**
 * Цель этого модуля, проверить валидацию.
 * Важен статус ответа, а не тело. Тело проверяем при тестировании эндпоинтов модуля server.
 */

@Slf4j
@WebMvcTest(controllers = ItemControllerGateway.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerGatewayTests {
    static final int LENGTH_NAME_NEW_ITEM = 10;
    static final int LENGTH_DESCR_NEW_ITEM = 50;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    ItemClient itemClient;

    @Test
    public void add() throws Exception {
        when(itemClient.add(any(NewItemRequest.class), anyLong()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        NewItemRequest newItemRequest = getNewItemRequest(true);

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(post(REQUEST_MAPPING_PATH_ITEM)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(newItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных - user id = 0.
        mvc.perform(post(REQUEST_MAPPING_PATH_ITEM)
                        .header(NAME_HEADER_USER_ID, "0")
                        .content(mapper.writeValueAsString(newItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Проверим, как пройдет валидация НЕ корректных данных, например пустое имя.
        newItemRequest.setName("");

        mvc.perform(post(REQUEST_MAPPING_PATH_ITEM)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(newItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private NewItemRequest getNewItemRequest(Boolean available) {
        NewItemRequest itemRequest = new NewItemRequest();
        itemRequest.setName(RandomStringUtils.randomAlphabetic(LENGTH_NAME_NEW_ITEM));
        itemRequest.setDescription(RandomStringUtils.randomAlphabetic(LENGTH_DESCR_NEW_ITEM));
        itemRequest.setAvailable(available);

        return itemRequest;
    }

    private UpdateItemRequest getUpdateItemRequest(Boolean available) {
        NewItemRequest newItemRequest = getNewItemRequest(available);

        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName(newItemRequest.getName());
        updateItemRequest.setDescription(newItemRequest.getDescription());
        updateItemRequest.setAvailable(newItemRequest.getAvailable());

        return updateItemRequest;
    }

    @Test
    public void findById() throws Exception {
        when(itemClient.getById(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(get(REQUEST_MAPPING_PATH_ITEM + "/1")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных - user id = 0.
        mvc.perform(get(REQUEST_MAPPING_PATH_ITEM + "/1")
                        .header(NAME_HEADER_USER_ID, "0")
                        .content(mapper.writeValueAsString(""))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Проверим, как пройдет валидация НЕ корректных данных - item id = 0.
        mvc.perform(get(REQUEST_MAPPING_PATH_ITEM + "/0")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(""))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findAll() throws Exception {
        when(itemClient.findAll(anyLong()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(get(REQUEST_MAPPING_PATH_ITEM)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных.
        mvc.perform(get(REQUEST_MAPPING_PATH_ITEM)
                        .header(NAME_HEADER_USER_ID, "-1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findAllByText() throws Exception {
        when(itemClient.findAllByText(anyString()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(get(REQUEST_MAPPING_PATH_ITEM + SEPARATOR + PATH_VARIABLE_SEARCH + "?text=12345")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void update() throws Exception {
        when(itemClient.update(anyLong(), any(UpdateItemRequest.class), anyLong()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        UpdateItemRequest updateItemRequest = getUpdateItemRequest(true);

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(patch(REQUEST_MAPPING_PATH_ITEM + "/1")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(updateItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных, например id item = 0.
        mvc.perform(patch(REQUEST_MAPPING_PATH_ITEM + "/0")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(updateItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Проверим, как пройдет валидация НЕ корректных данных, например id user = 0.
        mvc.perform(patch(REQUEST_MAPPING_PATH_ITEM + "/1")
                        .header(NAME_HEADER_USER_ID, "0")
                        .content(mapper.writeValueAsString(updateItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteTest() throws Exception {
        when(itemClient.delete(anyLong(), anyLong()))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(delete(REQUEST_MAPPING_PATH_ITEM + "/1")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(""))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных, например id user = 0.
        mvc.perform(delete(REQUEST_MAPPING_PATH_ITEM + "/1")
                        .header(NAME_HEADER_USER_ID, "0")
                        .content(mapper.writeValueAsString(""))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Проверим, как пройдет валидация НЕ корректных данных, например id item = 0.
        mvc.perform(delete(REQUEST_MAPPING_PATH_ITEM + "/0")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(""))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addComment() throws Exception {
        when(itemClient.addComment(anyLong(), anyLong(), any(NewCommentRequest.class)))
                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));

        NewCommentRequest newCommentRequest = new NewCommentRequest();
        newCommentRequest.setText("12345");

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(post(REQUEST_MAPPING_PATH_ITEM + "/1/comment")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(newCommentRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Проверим, как пройдет валидация НЕ корректных данных, например id item = 0.
        mvc.perform(post(REQUEST_MAPPING_PATH_ITEM + "/0/comment")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(newCommentRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Проверим, как пройдет валидация НЕ корректных данных, например id user = 0.
        mvc.perform(post(REQUEST_MAPPING_PATH_ITEM + "/1/comment")
                        .header(NAME_HEADER_USER_ID, "0")
                        .content(mapper.writeValueAsString(newCommentRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        // Проверим, как пройдет валидация НЕ корректных данных, например комментарий пустая строка.
        newCommentRequest.setText("");
        mvc.perform(post(REQUEST_MAPPING_PATH_ITEM + "/1/comment")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(newCommentRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

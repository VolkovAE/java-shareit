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
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.service.ItemDBServiceImpl;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.StringConstantsForRequest.*;

@Slf4j
@WebMvcTest(controllers = ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerServerTests {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    ItemDBServiceImpl itemService;

    private static ItemDto itemDto;
    private static NewItemRequest newItemRequest;
    private static UpdateItemRequest updateItemRequest;
    private static ItemWithDateDto itemWithDateDto;
    private static CommentDto commentDto;

    @BeforeAll
    public static void init() {
        String name = "name";
        String description = "description";

        itemDto = new ItemDto(1L, name, description, true);

        newItemRequest = new NewItemRequest();
        newItemRequest.setName(name);
        newItemRequest.setDescription(description);
        newItemRequest.setAvailable(itemDto.getAvailable());

        updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName(name);
        updateItemRequest.setDescription(description);
        updateItemRequest.setAvailable(itemDto.getAvailable());

        itemWithDateDto = new ItemWithDateDto(itemDto.getId(), itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(), null, null, null);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Отлично!");
        commentDto.setItem(itemDto);
        commentDto.setAuthorName(name);
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    public void add() throws Exception {
        when(itemService.add(any(NewItemRequest.class), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(post(REQUEST_MAPPING_PATH_ITEM)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(newItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class));
    }

    @Test
    public void findById() throws Exception {
        when(itemService.getById(anyLong(), anyLong()))
                .thenReturn(itemWithDateDto);

        mvc.perform(get(REQUEST_MAPPING_PATH_ITEM + "/1")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemWithDateDto.getId()), Long.class));
    }

    @Test
    public void findAll() throws Exception {
        when(itemService.findAll(anyLong()))
                .thenReturn(List.of(itemWithDateDto));

        mvc.perform(get(REQUEST_MAPPING_PATH_ITEM)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemWithDateDto.getId()), Long.class));
    }

    @Test
    public void findAllByText() throws Exception {
        when(itemService.findAllByText(anyString()))
                .thenReturn(List.of(itemDto));

        mvc.perform(get(REQUEST_MAPPING_PATH_ITEM + SEPARATOR + PATH_VARIABLE_SEARCH + "?text=12345")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class));
    }

    @Test
    public void update() throws Exception {
        when(itemService.update(anyLong(), any(UpdateItemRequest.class), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(patch(REQUEST_MAPPING_PATH_ITEM + "/1")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(updateItemRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class));
    }

    @Test
    public void deleteTest() throws Exception {
        when(itemService.delete(anyLong(), anyLong()))
                .thenReturn(itemDto);

        mvc.perform(delete(REQUEST_MAPPING_PATH_ITEM + "/1")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(""))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class));
    }

    @Test
    public void addComment() throws Exception {
        when(itemService.addComment(anyLong(), anyLong(), any(NewCommentRequest.class)))
                .thenReturn(commentDto);

        mvc.perform(post(REQUEST_MAPPING_PATH_ITEM + "/1/comment")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class));
    }
}

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
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestItem;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.StringConstantsForRequest.NAME_HEADER_USER_ID;
import static ru.practicum.shareit.util.StringConstantsForRequest.REQUEST_MAPPING_PATH_REQUEST;

@Slf4j
@WebMvcTest(controllers = ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerServerTests {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    ItemRequestServiceImpl requestService;

    private static NewRequestItem newRequestItem;
    private static ItemRequestDto itemRequestDto;

    @BeforeAll
    public static void init() {
        String description = "12345";

        newRequestItem = new NewRequestItem();
        newRequestItem.setDescription(description);

        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription(description);
    }

    @Test
    public void add() throws Exception {
        when(requestService.add(any(NewRequestItem.class), anyLong()))
                .thenReturn(itemRequestDto);

        mvc.perform(post(REQUEST_MAPPING_PATH_REQUEST)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(newRequestItem))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class));
    }

    @Test
    public void findAllMy() throws Exception {
        when(requestService.findAllMy(anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get(REQUEST_MAPPING_PATH_REQUEST)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class));
    }

    @Test
    public void findAllOther() throws Exception {
        when(requestService.findAllOther(anyLong()))
                .thenReturn(List.of(itemRequestDto));

        mvc.perform(get(REQUEST_MAPPING_PATH_REQUEST + "/all")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id", is(itemRequestDto.getId()), Long.class));
    }

    @Test
    public void findById() throws Exception {
        when(requestService.findById(anyLong()))
                .thenReturn(itemRequestDto);

        // Проверим, как пройдет валидация корректных данных.
        mvc.perform(get(REQUEST_MAPPING_PATH_REQUEST + "/1")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class));
    }
}

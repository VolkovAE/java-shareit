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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.NewBookingRequest;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.StringConstantsForRequest.NAME_HEADER_USER_ID;
import static ru.practicum.shareit.util.StringConstantsForRequest.REQUEST_PARAM_STATE;

/**
 * Цель этого модуля, проверить валидацию.
 * Важен статус ответа, а не тело. Тело проверяем при тестировании эндпоинтов модуля server.
 */

@Slf4j
@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerServerTests {
//    @Autowired
//    ObjectMapper mapper;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    BookingClient bookingClient;
//
//    @Test
//    public void add() throws Exception {
//        when(bookingClient.add(any(NewBookingRequest.class), anyLong()))
//                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));
//
//        NewBookingRequest newBookingRequest = getNewBookingRequest(1L);
//
//        // Проверим, как пройдет валидация корректных данных.
//        mvc.perform(post(REQUEST_MAPPING_PATH_BOOKING)
//                        .header(NAME_HEADER_USER_ID, "1")
//                        .content(mapper.writeValueAsString(newBookingRequest))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        // Проверим, как пройдет валидация НЕ корректных данных - user id = 0.
//        mvc.perform(post(REQUEST_MAPPING_PATH_BOOKING)
//                        .header(NAME_HEADER_USER_ID, "0")
//                        .content(mapper.writeValueAsString(newBookingRequest))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//
//        // Проверим, как пройдет валидация НЕ корректных данных - не указан конец аренды.
//        newBookingRequest.setEnd(null);
//
//        mvc.perform(post(REQUEST_MAPPING_PATH_BOOKING)
//                        .header(NAME_HEADER_USER_ID, "1")
//                        .content(mapper.writeValueAsString(newBookingRequest))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    private NewBookingRequest getNewBookingRequest(Long itemId) {
//        NewBookingRequest newBookingRequest = new NewBookingRequest();
//
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime start = now.plusSeconds(5);
//        LocalDateTime end = start.plusSeconds(10);
//
//        newBookingRequest.setStart(start);
//        newBookingRequest.setEnd(end);
//        newBookingRequest.setItemId(itemId);
//
//        return newBookingRequest;
//    }
//
//    @Test
//    public void approve() throws Exception {
//        when(bookingClient.approve(anyLong(), anyLong(), anyBoolean()))
//                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));
//
//        // Проверим, как пройдет валидация корректных данных.
//        mvc.perform(patch(REQUEST_MAPPING_PATH_BOOKING + "/1?approved=true")
//                        .header(NAME_HEADER_USER_ID, "1")
//                        .content(mapper.writeValueAsString(""))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        // Проверим, как пройдет валидация НЕ корректных данных, например нет параметра запроса approved.
//        mvc.perform(patch(REQUEST_MAPPING_PATH_BOOKING + "/1")
//                        .header(NAME_HEADER_USER_ID, "1")
//                        .content(mapper.writeValueAsString(""))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void findById() throws Exception {
//        when(bookingClient.findById(anyLong(), anyLong()))
//                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));
//
//        // Проверим, как пройдет валидация корректных данных.
//        mvc.perform(get(REQUEST_MAPPING_PATH_BOOKING + "/1")
//                        .header(NAME_HEADER_USER_ID, "1")
//                        .content("{}")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        // Проверим, как пройдет валидация НЕ корректных данных, например id user = 0.
//        mvc.perform(get(REQUEST_MAPPING_PATH_BOOKING + "/1")
//                        .header(NAME_HEADER_USER_ID, "0")
//                        .content("{}")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//
//        // Проверим, как пройдет валидация НЕ корректных данных, например id booking = 0.
//        mvc.perform(get(REQUEST_MAPPING_PATH_BOOKING + "/0")
//                        .header(NAME_HEADER_USER_ID, "1")
//                        .content("{}")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void findByBooker() throws Exception {
//        when(bookingClient.findByBooker(anyLong(), anyString()))
//                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));
//
//        // Проверим, как пройдет валидация корректных данных.
//        mvc.perform(get(REQUEST_MAPPING_PATH_BOOKING + "?" + REQUEST_PARAM_STATE + "=WAITING")
//                        .header(NAME_HEADER_USER_ID, "1")
//                        .content("{}")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        // Проверим, как пройдет валидация НЕ корректных данных, например id user = 0.
//        mvc.perform(get(REQUEST_MAPPING_PATH_BOOKING + "?" + REQUEST_PARAM_STATE + "=WAITING")
//                        .header(NAME_HEADER_USER_ID, "0")
//                        .content("{}")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    public void findByOwner() throws Exception {
//        when(bookingClient.findByOwner(anyLong(), anyString()))
//                .thenReturn(new ResponseEntity<>("", HttpStatus.OK));
//
//        // Проверим, как пройдет валидация корректных данных.
//        mvc.perform(get(REQUEST_MAPPING_PATH_BOOKING + SEPARATOR + PATH_VARIABLE_OWNER + "?" + REQUEST_PARAM_STATE + "=WAITING")
//                        .header(NAME_HEADER_USER_ID, "1")
//                        .content("{}")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//
//        // Проверим, как пройдет валидация НЕ корректных данных, например id user = 0.
//        mvc.perform(get(REQUEST_MAPPING_PATH_BOOKING + SEPARATOR + PATH_VARIABLE_OWNER + "?" + REQUEST_PARAM_STATE + "=WAITING")
//                        .header(NAME_HEADER_USER_ID, "0")
//                        .content("{}")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//    }
}

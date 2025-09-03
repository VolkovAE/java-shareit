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
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.service.BookingServiceImpl;

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
@WebMvcTest(controllers = BookingController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingControllerServerTests {
    @Autowired
    ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    BookingServiceImpl bookingService;

    private static NewBookingRequest newBookingRequest;
    private static BookingDto bookingDto;

    @BeforeAll
    public static void init() {
        newBookingRequest = new NewBookingRequest();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusSeconds(5);
        LocalDateTime end = start.plusSeconds(10);

        newBookingRequest.setStart(start);
        newBookingRequest.setEnd(end);
        newBookingRequest.setItemId(1L);

        bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(newBookingRequest.getStart());
        bookingDto.setEnd(newBookingRequest.getEnd());
    }

    @Test
    public void add() throws Exception {
        when(bookingService.add(any(NewBookingRequest.class), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(post(REQUEST_MAPPING_PATH_BOOKING)
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(newBookingRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));
    }

    @Test
    public void approve() throws Exception {
        when(bookingService.approve(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch(REQUEST_MAPPING_PATH_BOOKING + "/1?approved=true")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content(mapper.writeValueAsString(""))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));
    }

    @Test
    public void findById() throws Exception {
        when(bookingService.findById(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get(REQUEST_MAPPING_PATH_BOOKING + "/1")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));
    }

    @Test
    public void findByBooker() throws Exception {
        when(bookingService.findByBookerOrOwner(anyLong(), anyString(), anyBoolean()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get(REQUEST_MAPPING_PATH_BOOKING + "?" + REQUEST_PARAM_STATE + "=WAITING")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class));
    }

    @Test
    public void findByOwner() throws Exception {
        when(bookingService.findByBookerOrOwner(anyLong(), anyString(), anyBoolean()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get(REQUEST_MAPPING_PATH_BOOKING + SEPARATOR + PATH_VARIABLE_OWNER + "?" + REQUEST_PARAM_STATE + "=WAITING")
                        .header(NAME_HEADER_USER_ID, "1")
                        .content("{}")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class));
    }
}

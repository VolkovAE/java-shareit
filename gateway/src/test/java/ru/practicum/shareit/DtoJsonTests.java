package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.user.dto.NewUserRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DtoJsonTests {
    static final int LENGTH_NAME_NEW_USER = 10;
    static final int LENGTH_EMAIL_NEW_USER = 15;
    static final String NAME_DOMAIN = "yandex.ru";

    private final JacksonTester<NewUserRequest> jsonNewUserRequest;
    private final JacksonTester<NewBookingRequest> jsonNewBookingRequest;

    @Test
    void testUserDto() throws Exception {
        NewUserRequest newUserRequest = getNewUserRequest();

        JsonContent<NewUserRequest> result = jsonNewUserRequest.write(newUserRequest);

        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(newUserRequest.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(newUserRequest.getEmail());
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

    @Test
    void testFormatNewBookingRequest() throws Exception {
        String start = "2025-08-30T16:27:52";
        String end = "2025-08-30T16:27:53";

        NewBookingRequest newBookingRequest = new NewBookingRequest();
        newBookingRequest.setItemId(1L);
        newBookingRequest.setStart(LocalDateTime.parse(start, DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        newBookingRequest.setEnd(LocalDateTime.parse(end, DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        JsonContent<NewBookingRequest> result = jsonNewBookingRequest.write(newBookingRequest);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start);
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end);
    }
}

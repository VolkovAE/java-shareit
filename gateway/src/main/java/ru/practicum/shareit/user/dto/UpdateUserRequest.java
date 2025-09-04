package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

import static ru.practicum.shareit.util.NumericConstantsForRequest.LENGTH_EMAIL_USER_MAX;
import static ru.practicum.shareit.util.NumericConstantsForRequest.LENGTH_NAME_USER_MAX;

/// Данный класс используется для валидации данных при запросе обновления данных пользователя и последующей передачи его объектов на сервер.
/// Поэтому в отличие от серверной части, полям установлено доступность для сериализации и десериализации.
@Data
@EqualsAndHashCode(of = {"email"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Email(message = "Email is not valid")
    @Size(max = LENGTH_EMAIL_USER_MAX)
    @FieldDescription("Электронная почта")
    String email;

    @JsonProperty(access = JsonProperty.Access.READ_WRITE)
    @Size(max = LENGTH_NAME_USER_MAX)
    @FieldDescription("Имя для отображения")
    String name;
}

package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.validation.FieldDescription;

@Data
@EqualsAndHashCode(of = {"email"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserRequest {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Email(message = "Email is not valid")
    @FieldDescription("Электронная почта")
    String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @FieldDescription("Имя для отображения")
    String name;
}

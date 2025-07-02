package ru.practicum.shareit.request.model;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */
@Data
public class ItemRequest {
    Long id;    // уникальный идентификатор запроса;
    String description; // текст запроса, содержащий описание требуемой вещи;
    User requestor; // пользователь, создавший запрос;
    Instant created;    // дата и время создания запроса.
}

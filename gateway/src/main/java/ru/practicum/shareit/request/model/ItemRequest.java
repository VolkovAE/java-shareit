package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * Sprint add-item-requests.
 */
@Entity
@Table(name = "Requests")
@Setter
@Getter
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;    // уникальный идентификатор запроса;

    @Column(name = "description")
    String description; // текст запроса, содержащий описание требуемой вещи;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "requestor_id", nullable = false)
    User requestor; // пользователь, создавший запрос;

    @Column(name = "created")
    Instant created;    // дата и время создания запроса.
}

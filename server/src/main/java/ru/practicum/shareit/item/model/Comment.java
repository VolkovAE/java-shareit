package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.FieldDescription;
import ru.practicum.shareit.validation.Marker;

import java.time.Instant;

@Entity
@Table(name = "Comments")
@Setter
@Getter
@EqualsAndHashCode(of = {"id"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Null(message = "При создании комментария id формируется автоматически.", groups = Marker.OnCreate.class)
    @NotNull(message = "При обновлении данных в комментарии должен быть указан его id.",
            groups = {Marker.OnUpdate.class, Marker.OnDelete.class})
    @FieldDescription(value = "Уникальный идентификатор комментария", changeByCopy = false)
    Long id;

    @Column(name = "text")
    @NotBlank(message = "Текст комментария не может быть пустым.", groups = Marker.OnCreate.class)
    @FieldDescription(value = "Содержимое комментария")
    String text;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", nullable = false)
    @FieldDescription(value = "Вещь, к которой относится комментарий")
    Item item;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    @FieldDescription(value = "Автор комментария")
    User author;

    @Column(name = "created")
    @NotNull(message = "При создании комментария должна быть указана его дата создания.", groups = Marker.OnCreate.class)
    @FieldDescription(value = "Дата создания комментария")
    Instant created;
}

package ru.practicum.shareit.item.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ForbindenCreateComment;
import ru.practicum.shareit.exception.InternalServerException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;
import ru.practicum.shareit.util.Reflection;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Qualifier("ItemDBServiceImpl")
public class ItemDBServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper itemMapper;

    private static final Logger log = LoggerFactory.getLogger(ItemDBServiceImpl.class);

    @Autowired
    public ItemDBServiceImpl(ItemRepository itemRepository,
                             UserRepository userRepository,
                             BookingRepository bookingRepository,
                             CommentRepository commentRepository,
                             ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto add(NewItemRequest itemRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        Item item = itemMapper.toItem(itemRequest, user);

        item = itemRepository.save(item);

        log.info("Добавлена новая вещь {}.", item);

        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        log.info("Предоставлены данные по вещи {}.", item);

        return itemMapper.toItemDto(item);
    }

    @Override
    public Collection<ItemWithDateDto> findAll(Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        log.info("Получен список вещей пользователя {}.", owner);

        Collection<Item> items = itemRepository.findByOwner(owner);

        Instant instantNow = Instant.now();

        return items.stream()
                .map(item -> {
                    Booking lastBooking = bookingRepository.findByItemLastBooking(item, instantNow);
                    Booking nextBooking = bookingRepository.findByItemNextBooking(item, instantNow);

                    Instant lastStart = Objects.isNull(lastBooking) ? null : lastBooking.getStart();
                    Instant nextStart = Objects.isNull(nextBooking) ? null : nextBooking.getStart();

                    return itemMapper.toItemWithDateDto(item, lastStart, nextStart);
                })
                .toList();
    }

    @Override
    public Collection<ItemDto> findAllByText(String textSearch) {
        if (textSearch.isBlank()) return List.of();

        log.info("Получен список вещей доступных к аренде по строке поиска {}.", textSearch);

        return itemRepository.findAllByText(textSearch);
    }

    @Override
    public ItemDto update(Long itemId, UpdateItemRequest itemRequest, Long userId) {
        // проверяем, что вещь с таким id существует
        Item oldItem = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        // проверяем, что пользователь с таким id существует
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        // проверяем выполнение правила: Редактировать вещь может только её владелец.
        if (!user.equals(oldItem.getOwner()))
            throw new InternalServerException("Редактировать данные по вещи может только ее владелец." +
                    "Пользователь " + user +
                    "не является владельцем вещи " + oldItem + "." +
                    "Редактирование данных по вещи запрещено.");

        Item newItem = itemMapper.toItem(itemId, itemRequest, user);

        // обновляем содержимое
        BeanUtils.copyProperties(newItem, oldItem, Reflection.getIgnoreProperties(newItem));

        oldItem = itemRepository.save(oldItem);

        log.info("Обновлены данные по вещи {}.", oldItem);

        return itemMapper.toItemDto(oldItem);
    }

    @Override
    public ItemDto delete(final Long itemId, final Long userId) {
        // проверяем, что вещь с таким id существует
        Item removeItem = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        // проверяем, что пользователь с таким id существует
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        // проверяем выполнение правила: Редактировать вещь может только её владелец.
        if (!user.equals(removeItem.getOwner()))
            throw new InternalServerException("Удалить данные по вещи может только ее владелец." +
                    "Пользователь " + user +
                    "не является владельцем вещи " + removeItem + "." +
                    "Удаление данных по вещи запрещено.");

        itemRepository.delete(removeItem);

        log.info("Удалены данные по вещи {}.", removeItem);

        return itemMapper.toItemDto(removeItem);
    }

    @Override
    public Boolean isOwner(Item item, Long userId) {
        return item.getOwner().getId().equals(userId);
    }

    @Override
    public CommentDto addComment(Long itemId, Long userId, NewCommentRequest commentRequest) {
        // проверяем, что пользователь с таким id существует
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        // проверяем, что вещь с таким id существует
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);

        // проверяем, что пользователь арендовал вещь и заявка закрыта
        Booking booking = bookingRepository.findByItemAndBookerFirstCloseBooking(item, user, Instant.now());
        if (Objects.isNull(booking))
            throw new ForbindenCreateComment("У пользователя с id = " + userId + " нет закрытых заявок на " +
                    "аренду вещи с id = " + itemId + ". В создании отзыва отказано.", log);

        Comment comment = itemMapper.toComment(commentRequest, item, user);

        comment = commentRepository.save(comment);

        log.info("Пользователем {} добавлен комментарий {} к арендованной им ранее вещи {}.", user, comment, item);

        return itemMapper.toCommentDto(comment);
    }
}

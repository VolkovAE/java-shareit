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
import ru.practicum.shareit.item.model.DateLastNextBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentRepository;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
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
    private final ItemRequestRepository itemRequestRepository;
    private final ItemMapper itemMapper;

    private static final Logger log = LoggerFactory.getLogger(ItemDBServiceImpl.class);

    @Autowired
    public ItemDBServiceImpl(ItemRepository itemRepository,
                             UserRepository userRepository,
                             BookingRepository bookingRepository,
                             CommentRepository commentRepository,
                             ItemRequestRepository itemRequestRepository,
                             ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.commentRepository = commentRepository;
        this.itemRequestRepository = itemRequestRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDto add(NewItemRequest itemRequest, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        ItemRequest request = Objects.isNull(itemRequest.getRequestId()) ? null :
                itemRequestRepository.findById(itemRequest.getRequestId()).orElseThrow(
                        () -> new NotFoundException("Запрос на вещь с id = " + itemRequest.getRequestId() + " не найден.", log));

        Item item = itemMapper.toItem(itemRequest, user, request);

        item = itemRepository.save(item);

        log.info("Добавлена новая вещь {}.", item);

        return itemMapper.toItemDto(item);
    }

    @Override
    public ItemWithDateDto getById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + itemId + " не найдена.", log));

        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        List<Comment> comments = commentRepository.findByItemOrderByCreatedDesc(item);
        List<CommentDto> commentDtoList = Objects.isNull(comments) ? null : comments.stream().map(itemMapper::toCommentDto).toList();

        log.info("Предоставлены данные по вещи (с датами null посл./след. аренды) {} для пользователя {}.", item, user);

        return itemMapper.toItemWithDateDto(item, new DateLastNextBooking(), commentDtoList);
    }

    @Override
    public Collection<ItemWithDateDto> findAll(Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        Collection<Item> items = itemRepository.findByOwner(owner);

        Instant instantNow = Instant.now();

        log.info("Получен список вещей (с датами посл./след. аренды) владельца {}.", owner);

        return items.stream()
                .map(item -> {
                    DateLastNextBooking dateLastNextBooking = new DateLastNextBooking(item, bookingRepository, instantNow);

                    List<Comment> comments = commentRepository.findByItemOrderByCreatedDesc(item);
                    List<CommentDto> commentDtoList = Objects.isNull(comments) ? null : comments.stream().map(itemMapper::toCommentDto).toList();

                    return itemMapper.toItemWithDateDto(item, dateLastNextBooking, commentDtoList);
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

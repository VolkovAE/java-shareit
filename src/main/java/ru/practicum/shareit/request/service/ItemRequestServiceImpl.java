package ru.practicum.shareit.request.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemOnRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestItem;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Qualifier("ItemRequestServiceImpl")
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;


    private static final Logger log = LoggerFactory.getLogger(ItemRequestServiceImpl.class);

    @Autowired
    public ItemRequestServiceImpl(UserRepository userRepository,
                                  ItemRepository itemRepository,
                                  ItemMapper itemMapper,
                                  ItemRequestRepository itemRequestRepository,
                                  ItemRequestMapper itemRequestMapper
    ) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.itemRequestRepository = itemRequestRepository;
        this.itemRequestMapper = itemRequestMapper;
    }

    @Override
    public ItemRequestDto add(NewRequestItem requestItem, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        ItemRequest itemRequest = itemRequestMapper.toItemRequest(requestItem, user);

        itemRequest = itemRequestRepository.save(itemRequest);

        log.info("Добавлен новый запрос на не найденную вещь {}.", itemRequest);

        return itemRequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public Collection<ItemRequestDto> findAllMy(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        List<ItemRequest> itemRequestList = itemRequestRepository.findByRequestorOrderByCreatedDesc(user);

        log.info("Получен список запросов вещей пользователя {}.", user);

        return itemRequestList.stream()
                .map(itemRequest -> {
                    Collection<Item> items = itemRepository.findByRequestAndAvailableTrue(itemRequest);
                    List<ItemOnRequestDto> itemOnRequestDtoList = Objects.isNull(items) ? null : items.stream().map(itemMapper::toItemOnRequestDto).toList();

                    return itemRequestMapper.toItemRequestDto(itemRequest, itemOnRequestDtoList);
                })
                .toList();
    }

    @Override
    public Collection<ItemRequestDto> findAllOther(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        List<ItemRequest> itemRequestList = itemRequestRepository.findByRequestorNotOrderByCreatedDesc(user);

        log.info("Получен список запросов вещей НЕ пользователя {}.", user);

        return itemRequestList.stream()
                .map(itemRequestMapper::toItemRequestDto)
                .toList();
    }

    @Override
    public ItemRequestDto findById(Long requestId) {
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(
                () -> new NotFoundException("Запрос на вещь с id = " + requestId + " не найден.", log));

        Collection<Item> items = itemRepository.findByRequestAndAvailableTrue(itemRequest);
        List<ItemOnRequestDto> itemOnRequestDtoList = Objects.isNull(items) ? null : items.stream().map(itemMapper::toItemOnRequestDto).toList();

        return itemRequestMapper.toItemRequestDto(itemRequest, itemOnRequestDtoList);
    }
}

package ru.practicum.shareit.request.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestItem;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

@Service
@Qualifier("ItemRequestServiceImpl")
public class ItemRequestServiceImpl implements ItemRequestService {
    private final UserRepository userRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRequestMapper itemRequestMapper;

    private static final Logger log = LoggerFactory.getLogger(ItemRequestServiceImpl.class);

    @Autowired
    public ItemRequestServiceImpl(UserRepository userRepository,
                                  ItemRequestRepository itemRequestRepository,
                                  ItemRequestMapper itemRequestMapper) {
        this.userRepository = userRepository;
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
        return null;    // todo
    }

    @Override
    public Collection<ItemRequestDto> findAllOther(Long userId) {
        return null;    // todo
    }

    @Override
    public ItemRequestDto findById(Long requestId) {
        return null;    // todo
    }
}

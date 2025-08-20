package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestItem;

import java.util.Collection;

public interface ItemRequestService {
    ItemRequestDto add(NewRequestItem requestItem, Long userId);

    Collection<ItemRequestDto> findAllMy(Long userId);

    Collection<ItemRequestDto> findAllOther(Long userId);

    ItemRequestDto findById(Long requestId);
}

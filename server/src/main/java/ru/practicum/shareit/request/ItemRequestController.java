package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.NewRequestItem;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.validation.Marker;

import java.util.Collection;

import static ru.practicum.shareit.util.StringConstantsForRequest.NAME_HEADER_USER_ID;
import static ru.practicum.shareit.util.StringConstantsForRequest.PATH_VARIABLE_REQUEST_ID;

/**
 * Sprint add-item-requests.
 */
@Validated
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    private static final Logger log = LoggerFactory.getLogger(ItemRequestController.class);

    @Autowired
    public ItemRequestController(@Qualifier("ItemRequestServiceImpl") ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @PostMapping
    @Validated(Marker.OnCreate.class)
    ItemRequestDto add(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId,
                       @RequestBody @Valid NewRequestItem requestItem) {
        return itemRequestService.add(requestItem, userId);
    }

    @GetMapping
    public Collection<ItemRequestDto> findAllMy(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId) {
        return itemRequestService.findAllMy(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> findAllOther(@RequestHeader(name = NAME_HEADER_USER_ID, required = true) @Positive Long userId) {
        return itemRequestService.findAllOther(userId);
    }

    @GetMapping("/{requestId}")
    ItemRequestDto findById(@PathVariable(name = PATH_VARIABLE_REQUEST_ID, required = true) @Positive Long requestId) {
        return itemRequestService.findById(requestId);
    }
}

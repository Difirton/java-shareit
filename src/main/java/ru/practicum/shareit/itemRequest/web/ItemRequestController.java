package ru.practicum.shareit.itemRequest.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemRequest.repository.ItemRequest;
import ru.practicum.shareit.itemRequest.service.ItemRequestService;
import ru.practicum.shareit.itemRequest.web.convertor.ItemRequestDtoToItemRequestConvertor;
import ru.practicum.shareit.itemRequest.web.convertor.ItemRequestToItemRequestDtoConvertor;
import ru.practicum.shareit.itemRequest.web.dto.ItemRequestDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final ItemRequestDtoToItemRequestConvertor itemRequestFromDtoConvertor;
    private final ItemRequestToItemRequestDtoConvertor itemRequestToDtoConvertor;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ItemRequestDto createItemRequest(@RequestBody @Valid ItemRequestDto itemRequestDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemRequestDto.setUserId(userId);
        ItemRequest newItemRequest = itemRequestService.save(itemRequestFromDtoConvertor.convert(itemRequestDto));
        return itemRequestToDtoConvertor.convert(newItemRequest);
    }
}

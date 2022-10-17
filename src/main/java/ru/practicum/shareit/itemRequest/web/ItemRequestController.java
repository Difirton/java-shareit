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
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ItemRequestDto> getItemsRequests(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemRequest> allItemsRequests = itemRequestService.findAll(userId);
        return allItemsRequests.stream()
                .map(itemRequestToDtoConvertor::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemRequestDto getItemRequestById(@PathVariable("id") Long id) {
        return itemRequestToDtoConvertor.convert(itemRequestService.findById(id));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemRequestDto updateItemRequestById(@PathVariable("id") Long id, @RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody ItemRequestDto updateItemRequestDto) {
        updateItemRequestDto.setUserId(userId);
        ItemRequest updatedItem = itemRequestService.update(id, itemRequestFromDtoConvertor
                .convert(updateItemRequestDto));
        return itemRequestToDtoConvertor.convert(updatedItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteItemRequest(@PathVariable("id") Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemRequestService.deleteById(id, userId);
    }
}

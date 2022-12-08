package ru.practicum.shareit.item_request.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.item_request.service.ItemRequestService;
import ru.practicum.shareit.item_request.web.convertor.ItemRequestDtoToItemRequestConvertor;
import ru.practicum.shareit.item_request.web.convertor.ItemRequestToItemRequestAnswerWithItemsDtoConvertor;
import ru.practicum.shareit.item_request.web.convertor.ItemRequestToItemRequestDtoConvertor;
import ru.practicum.shareit.item_request.web.dto.ItemRequestAnswerWithItemsDto;
import ru.practicum.shareit.item_request.web.dto.ItemRequestDto;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final ItemRequestDtoToItemRequestConvertor itemRequestFromDtoConvertor;
    private final ItemRequestToItemRequestDtoConvertor itemRequestToDtoConvertor;
    private final ItemRequestToItemRequestAnswerWithItemsDtoConvertor toItemRequestAnswerWithItemsDtoConvertor;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";
    private static final int DEFAULT_SIZE = 10;
    private static final int DEFAULT_FROM = 0;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ItemRequestDto createItemRequest(@RequestBody ItemRequestDto itemRequestDto,
                                     @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        itemRequestDto.setUserId(userId);
        ItemRequest newItemRequest = itemRequestService.save(itemRequestFromDtoConvertor.convert(itemRequestDto));
        return itemRequestToDtoConvertor.convert(newItemRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ItemRequestAnswerWithItemsDto> getItemsRequests(@RequestHeader(USER_REQUEST_HEADER) Long userId) {
        List<ItemRequest> allItemsRequests = itemRequestService.findAll(userId);
        return allItemsRequests.stream()
                .map(toItemRequestAnswerWithItemsDtoConvertor::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    List<ItemRequestAnswerWithItemsDto> getPageableItemsRequests(@RequestHeader(USER_REQUEST_HEADER) Long userId,
                                                  @RequestParam Optional<Integer> from,
                                                  @RequestParam Optional<Integer> size) {
        List<ItemRequest> allPageableItemsRequests = itemRequestService.findAllPageable(userId,
                from.orElse(DEFAULT_FROM), size.orElse(DEFAULT_SIZE));
        return allPageableItemsRequests.stream()
                .map(toItemRequestAnswerWithItemsDtoConvertor::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemRequestAnswerWithItemsDto getItemRequestById(@PathVariable("id") Long id,
                                      @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return toItemRequestAnswerWithItemsDtoConvertor.convert(itemRequestService.findById(id, userId));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemRequestDto updateItemRequestById(@PathVariable("id") Long id, @RequestHeader(USER_REQUEST_HEADER) Long userId,
                           @RequestBody ItemRequestDto updateItemRequestDto) {
        updateItemRequestDto.setUserId(userId);
        ItemRequest updatedItem = itemRequestService.update(id, itemRequestFromDtoConvertor
                .convert(updateItemRequestDto));
        return itemRequestToDtoConvertor.convert(updatedItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteItemRequest(@PathVariable("id") Long id, @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        itemRequestService.deleteById(id, userId);
    }
}
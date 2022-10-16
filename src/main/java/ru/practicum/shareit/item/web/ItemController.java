package ru.practicum.shareit.item.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.web.convertor.ItemDtoToItemConverter;
import ru.practicum.shareit.item.web.convertor.ItemToItemDtoConverter;
import ru.practicum.shareit.item.web.dto.ItemDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {
    private final ItemService itemService;
    private final ItemDtoToItemConverter itemDtoToItemConverter;
    private final ItemToItemDtoConverter itemToItemDtoConverter;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemDto.setUserId(userId);
        Item newItem = itemService.save(itemDtoToItemConverter.convert(itemDto));
        return itemToItemDtoConverter.convert(newItem);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<Item> allItems = itemService.findAll(userId);
        return allItems.stream()
                .map(itemToItemDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemDto getItemById(@PathVariable("id") Long id) {
        return itemToItemDtoConverter.convert(itemService.findById(id));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemDto updateItemById(@PathVariable("id") Long id, @RequestHeader("X-Sharer-User-Id") Long userId,
                           @RequestBody ItemDto updateItemDto) {
        updateItemDto.setUserId(userId);
        Item updatedItem = itemService.update(id, itemDtoToItemConverter.convert(updateItemDto));
        return itemToItemDtoConverter.convert(updatedItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteItem(@PathVariable("id") Long id, @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemService.deleteById(id, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    List<ItemDto> getItemById(@RequestParam("text") String query) {
        List<Item> allItems = itemService.findByParam(query);
        return allItems.stream()
                .map(itemToItemDtoConverter::convert)
                .collect(Collectors.toList());
    }
}

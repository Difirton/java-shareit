package ru.practicum.shareit.item.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemManager;
import ru.practicum.shareit.item.web.dto.CommentDto;
import ru.practicum.shareit.item.web.dto.ItemDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/items", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {
    private final ItemManager itemManager;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ItemDto createItem(@RequestBody ItemDto itemDto, @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return itemManager.save(itemDto, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ItemDto> getItems(@RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return itemManager.findAll(userId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto getItemById(@PathVariable("id") Long id, @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return itemManager.findById(id, userId);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemDto updateItemById(@PathVariable("id") Long itemId, @RequestBody ItemDto itemDto,
                           @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return itemManager.update(itemId, userId, itemDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteItem(@PathVariable("id") Long itemId, @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        itemManager.deleteById(itemId, userId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    List<ItemDto> getItemByNameAndDescription(@RequestParam("text") String query) {
        return itemManager.findItemByParam(query);
    }

    @PostMapping(path = "/{itemId}/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    CommentDto createComment(@RequestBody CommentDto commentDto, @PathVariable("itemId") Long itemId,
                             @RequestHeader(USER_REQUEST_HEADER) Long bookerId) {
        return itemManager.saveComment(itemId, bookerId, commentDto);
    }
}

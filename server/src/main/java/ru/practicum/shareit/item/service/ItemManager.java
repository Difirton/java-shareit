package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.web.dto.CommentDto;
import ru.practicum.shareit.item.web.dto.ItemDto;

import java.util.List;

public interface ItemManager {
    ItemDto save(ItemDto itemDto, Long userId);

    List<ItemDto> findAll(Long userId);

    ItemDto setBookings(ItemDto itemDto);

    ItemDto findById(Long id, Long userId);

    ItemDto update(Long itemId, Long userId, ItemDto updateItemDto);

    void deleteById(Long itemId, Long userId);

    List<ItemDto> findItemByParam(String query);

    CommentDto saveComment(Long itemId, Long bookerId, CommentDto commentDto);
}

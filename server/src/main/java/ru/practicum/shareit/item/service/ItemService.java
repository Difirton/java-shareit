package ru.practicum.shareit.item.service;

import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.item.repository.Comment;
import ru.practicum.shareit.item.repository.Item;

import java.util.List;

@Validated
public interface ItemService {
    Item save(Item item);

    List<Item> findAll(Long userId);

    Item findById(Long id);

    Item update(Long id, Item itemToUpdate);

    void deleteById(Long id, Long userId);

    List<Item> findByParam(String query);

    Item findAvailableById(Long id);

    Item findAvailableRenter(Long id, Long renterId);

    Comment saveComment(Comment comment);

    List<Booking> findAllByItemId(Long id);
}

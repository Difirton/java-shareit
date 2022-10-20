package ru.practicum.shareit.item_request.service;

import ru.practicum.shareit.item_request.repository.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequest save(ItemRequest convert);

    List<ItemRequest> findAll(Long userId);

    ItemRequest findById(Long id);

    ItemRequest update(Long id, ItemRequest convert);

    void deleteById(Long id, Long userId);
}

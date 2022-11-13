package ru.practicum.shareit.item_request.repository;

import java.util.List;

public interface ItemRequestCustomRepository {

    List<ItemRequest> findWithItemAll();

    ItemRequest findWithItemById(Long id);
}

package ru.practicum.shareit.item.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
        super(String.format("Item with id = %s, not found", id));
        log.error("Item with id = {}, not found", id);
    }

    public ItemNotFoundException(Long id, Long userId) {
        super(String.format("User with id = %s hasn't item with id = %s", userId, id));
        log.error("User with id = {} hasn't item with id = {}", userId, id);
    }
}

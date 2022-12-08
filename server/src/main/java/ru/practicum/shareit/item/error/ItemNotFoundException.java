package ru.practicum.shareit.item.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long id) {
        super(String.format("Item with id = %s, not found", id));
        log.error("Item with id = {}, not found", id);
    }
}

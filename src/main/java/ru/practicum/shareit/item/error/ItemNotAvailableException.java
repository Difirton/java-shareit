package ru.practicum.shareit.item.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemNotAvailableException extends RuntimeException {

    public ItemNotAvailableException(Long id) {
        super(String.format("Item with id = %s is not available", id));
        log.error("Item with id = {} is not available", id);
    }
}

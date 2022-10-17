package ru.practicum.shareit.itemRequest.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemRequestNotAvailableException extends RuntimeException {

    public ItemRequestNotAvailableException(Long id) {
        super(String.format("Item request with id = %s is not available", id));
        log.error("Item request with id = {} is not available", id);
    }
}

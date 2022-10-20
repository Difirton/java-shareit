package ru.practicum.shareit.item_request.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemRequestAuthenticationException extends RuntimeException {

    public ItemRequestAuthenticationException(Long id) {
        super(String.format("Item request with id = %s is not available", id));
        log.error("Item request with id = {} is not available", id);
    }
}

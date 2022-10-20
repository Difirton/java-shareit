package ru.practicum.shareit.item.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemAuthenticationException extends RuntimeException {
    public ItemAuthenticationException(Long id) {
        super(String.format("Attempt to access change to item with id = %s without auth token", id));
        log.error("Attempt to access change to item with id = {} without auth token", id);
    }

    public ItemAuthenticationException(Long id, Long userId) {
        super(String.format("User with id = %s is not authorized to make changes to item with id = %s", userId, id));
        log.error("User with id = {} is not authorized to make changes to item with id = {}", userId, id);
    }
}

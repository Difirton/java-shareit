package ru.practicum.shareit.item_request.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ItemRequestNotFoundException extends RuntimeException {

    public ItemRequestNotFoundException(Long id) {
        super(String.format("Item request with id = %s, not found", id));
        log.error("Item request with id = {}, not found", id);
    }
}

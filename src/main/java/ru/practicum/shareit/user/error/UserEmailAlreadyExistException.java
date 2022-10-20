package ru.practicum.shareit.user.error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserEmailAlreadyExistException extends RuntimeException {

    public UserEmailAlreadyExistException(String email) {
        super(String.format("User with email = %s, already exist", email));
        log.error("User with email = {}, already exist", email);
    }
}

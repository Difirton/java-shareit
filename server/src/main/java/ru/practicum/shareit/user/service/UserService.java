package ru.practicum.shareit.user.service;

import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.repository.User;

import java.util.List;

@Validated
public interface UserService {
    User save(User user);

    List<User> findAll();

    User findById(Long id);

    User update(Long id, User userToUpdate);

    void deleteById(Long id);
}

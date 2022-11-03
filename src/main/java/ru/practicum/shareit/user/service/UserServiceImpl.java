package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.repository.User;
import ru.practicum.shareit.user.error.UserNotFoundException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.common.utill.NotNullPropertiesCopier;

import javax.validation.Valid;
import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, NotNullPropertiesCopier<User> {
    private final UserRepository userRepository;

    @Override
    public User save(@Valid User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User update(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        this.copyNotNullProperties(updatedUser, user);
        return userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }
}

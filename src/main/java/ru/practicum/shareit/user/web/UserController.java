package ru.practicum.shareit.user.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.web.dto.UserDto;
import ru.practicum.shareit.user.web.convertor.UserDtoToUserConverter;
import ru.practicum.shareit.user.web.convertor.UserToUserDtoConverter;
import ru.practicum.shareit.user.repository.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users",
                consumes = MediaType.ALL_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;
    private final UserDtoToUserConverter userDtoToUserConverter;
    private final UserToUserDtoConverter userToUserDtoConverter;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    UserDto createUser(@RequestBody UserDto userDto) {
        User newUser = userService.save(userDtoToUserConverter.convert(userDto));
        return userToUserDtoConverter.convert(newUser);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<UserDto> getUsers() {
        List<User> allUsers = userService.findAll();
        return allUsers.stream()
                .map(userToUserDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDto getUserById(@PathVariable("id") Long id) {
        return userToUserDtoConverter.convert(userService.findById(id));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDto updateUserById(@PathVariable("id") Long id, @RequestBody UserDto updateUserDto) {
        User updatedUser = userService.update(id, userDtoToUserConverter.convert(updateUserDto));
        return userToUserDtoConverter.convert(updatedUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
    }
}

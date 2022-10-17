package ru.practicum.shareit.user.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.web.convertor.UserDtoToUserConverter;
import ru.practicum.shareit.user.web.convertor.UserToUserDtoConverter;
import ru.practicum.shareit.user.repository.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.web.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "The user API", description = "API for interacting with endpoints associated with users")
@RequestMapping(path = "/users", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;
    private final UserDtoToUserConverter userDtoToUserConverter;
    private final UserToUserDtoConverter userToUserDtoConverter;

    @Operation(summary = "Creates a new user", tags = "The user API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The user was created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not valid parameters"),
            @ApiResponse(responseCode = "409", description = "User with this email already exist")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    UserDto createUser(@RequestBody UserDto userDto) {
        User newUser = userService.save(userDtoToUserConverter.convert(userDto));
        return userToUserDtoConverter.convert(newUser);
    }

    @Operation(summary = "Gets all users", tags = "The user API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Found all users",
                    content = {
                            @Content(mediaType = "application/json")
                    })
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<UserDto> getUsers() {
        List<User> allUsers = userService.findAll();
        return allUsers.stream()
                .map(userToUserDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get the user by his id, which is specified in URL", tags = "The user API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the requested user",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDto getUserById(@Parameter(description = "User ID") @PathVariable("id") Long id) {
        return userToUserDtoConverter.convert(userService.findById(id));
    }

    @Operation(summary = "Update the user by his id, which is specified in URL", tags = "The user API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The user was updated",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "403", description = "Do not have update permission")
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    UserDto updateUserById(@Parameter(description = "User ID") @PathVariable("id") Long id,
                           @RequestBody @Valid UserDto updateUserDto) {
        User updatedUser = userService.update(id, userDtoToUserConverter.convert(updateUserDto));
        return userToUserDtoConverter.convert(updatedUser);
    }

    @Operation(summary = "Removes the user by his id, which is specified in URL", tags = "The user API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The user was removed",
                    content = {
                            @Content(mediaType = "application/json")
                    })
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteUser(@Parameter(description = "User ID") @PathVariable("id") Long id) {
        userService.deleteById(id);
    }
}

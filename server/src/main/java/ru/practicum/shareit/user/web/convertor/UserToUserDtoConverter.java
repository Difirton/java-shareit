package ru.practicum.shareit.user.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.repository.User;

@Component
public class UserToUserDtoConverter  implements Converter<User, ru.practicum.shareit.user.web.dto.UserDto> {

    @Override
    public ru.practicum.shareit.user.web.dto.UserDto convert(User source) {
        return ru.practicum.shareit.user.web.dto.UserDto.builder()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail())
                .build();
    }
}

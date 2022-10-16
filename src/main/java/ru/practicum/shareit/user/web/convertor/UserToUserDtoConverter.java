package ru.practicum.shareit.user.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.repository.User;
import ru.practicum.shareit.user.web.dto.UserDto;

@Component
public class UserToUserDtoConverter  implements Converter<User, UserDto> {

    @Override
    public UserDto convert(User source) {
        return UserDto.builder()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail())
                .build();
    }
}

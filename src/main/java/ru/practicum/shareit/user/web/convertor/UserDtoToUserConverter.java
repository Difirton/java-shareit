package ru.practicum.shareit.user.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.repository.User;
import ru.practicum.shareit.user.web.dto.UserDto;

@Component
public class UserDtoToUserConverter implements Converter<UserDto, User> {
    @Override
    public User convert(UserDto source) {
        return User.builder()
                .id(source.getId())
                .name(source.getName())
                .email(source.getEmail())
                .build();
    }
}

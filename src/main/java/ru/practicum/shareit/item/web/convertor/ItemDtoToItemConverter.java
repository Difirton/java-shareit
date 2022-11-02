package ru.practicum.shareit.item.web.convertor;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.web.dto.ItemDto;
import ru.practicum.shareit.user.repository.User;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemDtoToItemConverter implements Converter<ItemDto, Item> {
    private final CommentDtoToCommentConverter commentDtoToCommentConverter;

    @Override
    public Item convert(ItemDto source) {
        return Item.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .owner(User.builder()
                        .id(source.getUserId())
                        .build())
                .available(source.getAvailable())
                .comments(source.getCommentsDto().stream()
                        .map(commentDtoToCommentConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}

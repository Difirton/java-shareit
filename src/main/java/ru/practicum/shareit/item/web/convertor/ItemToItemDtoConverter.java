package ru.practicum.shareit.item.web.convertor;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.web.dto.ItemDto;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemToItemDtoConverter implements Converter<Item, ItemDto> {
    private final CommentToCommentDtoConverter commentToCommentDtoConverter;

    @Override
    public ItemDto convert(Item source) {
        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .itemRequestId(source.getItemRequest() == null ? null : source.getItemRequest().getId())
                .commentsDto(source.getComments().stream()
                        .map(commentToCommentDtoConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}

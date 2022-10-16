package ru.practicum.shareit.itemRequest.web.convertor;

import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.itemRequest.repository.ItemRequest;
import ru.practicum.shareit.itemRequest.web.dto.ItemRequestDto;

public class ItemRequestToItemRequestDtoConvertor implements Converter<ItemRequest, ItemRequestDto> {
    @Override
    public ItemRequestDto convert(ItemRequest source) {
        return ItemRequestDto.builder()
                .id(source.getId())
                .description(source.getDescription())
                .created(source.getCreated())
                .userId(source.getUser().getId())
                .build();
    }
}

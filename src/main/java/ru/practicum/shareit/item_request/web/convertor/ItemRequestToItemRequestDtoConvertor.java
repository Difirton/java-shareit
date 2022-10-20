package ru.practicum.shareit.item_request.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.item_request.web.dto.ItemRequestDto;

@Component
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

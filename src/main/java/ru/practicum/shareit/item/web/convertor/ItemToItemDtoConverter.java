package ru.practicum.shareit.item.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.web.dto.ItemDto;

@Component
public class ItemToItemDtoConverter implements Converter<Item, ItemDto> {
    @Override
    public ItemDto convert(Item source) {
        return ItemDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .available(source.getAvailable())
                .build();
    }
}

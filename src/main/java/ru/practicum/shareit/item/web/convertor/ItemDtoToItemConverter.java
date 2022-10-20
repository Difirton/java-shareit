package ru.practicum.shareit.item.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.web.dto.ItemDto;
import ru.practicum.shareit.user.repository.User;

@Component
public class ItemDtoToItemConverter implements Converter<ItemDto, Item> {
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
                .build();
    }
}

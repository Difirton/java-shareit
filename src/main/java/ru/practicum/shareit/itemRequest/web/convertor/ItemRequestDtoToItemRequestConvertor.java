package ru.practicum.shareit.itemRequest.web.convertor;

import org.springframework.core.convert.converter.Converter;
import ru.practicum.shareit.itemRequest.repository.ItemRequest;
import ru.practicum.shareit.itemRequest.web.dto.ItemRequestDto;
import ru.practicum.shareit.user.repository.User;

public class ItemRequestDtoToItemRequestConvertor implements Converter<ItemRequestDto, ItemRequest> {
    @Override
    public ItemRequest convert(ItemRequestDto source) {
        return ItemRequest.builder()
                .id(source.getId())
                .description(source.getDescription())
                .created(source.getCreated())
                .user(User.builder()
                        .id(source.getUserId())
                        .build())
                .build();
    }
}

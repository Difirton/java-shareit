package ru.practicum.shareit.item_request.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.item_request.web.dto.ItemRequestDto;
import ru.practicum.shareit.user.repository.User;

@Component
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

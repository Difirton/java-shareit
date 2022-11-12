package ru.practicum.shareit.item_request.web.convertor;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.web.convertor.ItemToItemDtoConverter;
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.item_request.web.dto.ItemRequestAnswerWithItemsDto;

import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class ItemRequestToItemRequestAnswerWithItemsDtoConvertor implements
        Converter<ItemRequest, ItemRequestAnswerWithItemsDto> {
    private final ItemToItemDtoConverter itemToItemDtoConverter;

    @Override
    public ItemRequestAnswerWithItemsDto convert(ItemRequest source) {
        return ItemRequestAnswerWithItemsDto.builder()
                .id(source.getId())
                .description(source.getDescription())
                .created(source.getCreated())
                .userId(source.getUser().getId())
                .items(source.getItems().stream()
                        .map(itemToItemDtoConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}

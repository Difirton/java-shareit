package ru.practicum.shareit.item_request.web.dto;

import lombok.*;
import ru.practicum.shareit.item.web.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestAnswerWithItemsDto {
    private Long id;

    private String description;

    private LocalDateTime created;

    private Long userId;

    private List<ItemDto> items;
}

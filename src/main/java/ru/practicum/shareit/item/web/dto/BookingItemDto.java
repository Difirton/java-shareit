package ru.practicum.shareit.item.web.dto;

import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class BookingItemDto {
    private Long id;
    private Long bookerId;
}

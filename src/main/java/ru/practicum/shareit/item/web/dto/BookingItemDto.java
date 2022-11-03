package ru.practicum.shareit.item.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Booking history element")
public class BookingItemDto {
    @Schema(description = "Booking ID")
    private Long id;
    @Schema(description = "Booker ID")
    private Long bookerId;
}

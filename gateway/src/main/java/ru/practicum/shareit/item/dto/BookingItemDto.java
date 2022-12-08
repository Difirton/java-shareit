package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Booking history element")
public class BookingItemDto {
    @Schema(description = "Booking ID")
    private Long id;
    @Schema(description = "Booker ID")
    private Long bookerId;
}

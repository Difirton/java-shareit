package ru.practicum.shareit.booking.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.web.dto.ItemDto;
import ru.practicum.shareit.user.web.dto.UserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Booking")
public class BookingDto {
    @Schema(description = "Booking ID", example = "1")
    private Long id;

    @Schema(description = "Booking start", example = "2022-10-19T21:56:04", required = true)
    @FutureOrPresent
    private LocalDateTime start;

    @Schema(description = "Booking end", example = "2022-10-19T21:56:04", required = true)
    @Future
    @JsonProperty("end")
    private LocalDateTime finish;

    @Schema(description = "Status", example = "APPROVED")
    private Status status;

    @Schema(description = "Renter ID", example = "1")
    private Long renterId;

    @Schema(description = "Item ID", example = "1")
    private Long itemId;

    private UserDto booker;

    private ItemDto item;
}

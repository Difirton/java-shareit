package ru.practicum.shareit.booking.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BookingDto {
    private Long id;

    @FutureOrPresent
    private LocalDateTime start;

    @Future
    @JsonProperty("end")
    private LocalDateTime finish;

    private Status status;

    private Long renterId;

    private Long itemId;

    private UserDto booker;

    private ItemDto item;
}

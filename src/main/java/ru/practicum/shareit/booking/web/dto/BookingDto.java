package ru.practicum.shareit.booking.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.web.dto.ItemDto;
import ru.practicum.shareit.user.web.dto.UserDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
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

    private Status status;

    private Long renterId;

    private Long itemId;

    private UserDto booker;

    private ItemDto item;

    public static BookingDtoBuilder builder() {
        return new BookingDtoBuilder();
    }

    public static class BookingDtoBuilder {
        private Long id;
        @FutureOrPresent
        private LocalDateTime start;
        @Future
        private LocalDateTime finish;
        private Status status;
        private Long renterId;
        private Long itemId;

        @JsonIdentityInfo(
                generator = ObjectIdGenerators.PropertyGenerator.class,
                property = "id")
        private UserDto booker;

        @JsonIdentityInfo(
                generator = ObjectIdGenerators.PropertyGenerator.class,
                property = "id")
        private ItemDto item;

        public BookingDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BookingDtoBuilder start(@FutureOrPresent LocalDateTime start) {
            this.start = start;
            return this;
        }

        public BookingDtoBuilder finish(@Future LocalDateTime finish) {
            this.finish = finish;
            return this;
        }

        public BookingDtoBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public BookingDtoBuilder renterId(Long renterId) {
            this.renterId = renterId;
            return this;
        }

        public BookingDtoBuilder itemId(Long itemId) {
            this.itemId = itemId;
            return this;
        }

        public BookingDtoBuilder booker(UserDto booker) {
            this.booker = booker;
            return this;
        }

        public BookingDtoBuilder item(ItemDto item) {
            this.item = item;
            return this;
        }

        public BookingDto build() {
            return new BookingDto(id, start, finish, status, renterId, itemId, booker, item);
        }
    }
}

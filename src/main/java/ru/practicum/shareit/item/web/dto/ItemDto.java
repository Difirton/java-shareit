package ru.practicum.shareit.item.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.web.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item")
public class ItemDto {
    @Schema(description = "Item ID", example = "1")
    private Long id;

    @Schema(description = "Item name", example = "example", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Item description", example = "example", required = true)
    @NotBlank
    private String description;

    @Schema(description = "Is item available", example = "true", required = true)
    @NotNull
    private Boolean available;

    @JsonIgnore
    private Long userId;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    private BookingItemDto nextBooking;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    private BookingItemDto lastBooking;

    public static ItemDtoBuilder builder() {
        return new ItemDtoBuilder();
    }

    public static class ItemDtoBuilder {
        private Long id;
        @NotBlank
        private String name;
        @NotBlank
        private String description;
        @NotNull
        private Boolean available;
        private Long userId;

        private BookingItemDto nextBooking;

        private BookingItemDto lastBooking;

        public ItemDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ItemDtoBuilder name(@NotBlank String name) {
            this.name = name;
            return this;
        }

        public ItemDtoBuilder description(@NotBlank String description) {
            this.description = description;
            return this;
        }

        public ItemDtoBuilder available(@NotNull Boolean available) {
            this.available = available;
            return this;
        }

        public ItemDtoBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public ItemDtoBuilder nextBooking(BookingItemDto nextBooking) {
            this.nextBooking = nextBooking;
            return this;
        }

        public ItemDtoBuilder lastBooking(BookingItemDto lastBooking) {
            this.lastBooking = lastBooking;
            return this;
        }

        public ItemDto build() {
            return new ItemDto(id, name, description, available, userId, nextBooking, lastBooking);
        }
    }
}

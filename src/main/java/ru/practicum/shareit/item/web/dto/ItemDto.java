package ru.practicum.shareit.item.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        public ItemDto build() {
            return new ItemDto(id, name, description, available, userId);
        }
    }
}

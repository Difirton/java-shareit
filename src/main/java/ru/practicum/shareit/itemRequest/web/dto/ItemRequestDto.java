package ru.practicum.shareit.itemRequest.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.repository.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;

    @NotBlank
    private String description;

    private LocalDateTime created;

    private Long userId;

    public static ItemRequestDtoBuilder builder() {
        return new ItemRequestDtoBuilder();
    }

    public static class ItemRequestDtoBuilder {
        private Long id;
        @NotBlank
        private String description;
        private LocalDateTime created;
        private Long userId;

        private ItemRequestDtoBuilder() { }

        public ItemRequestDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ItemRequestDtoBuilder description(@NotBlank String description) {
            this.description = description;
            return this;
        }

        public ItemRequestDtoBuilder created(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public ItemRequestDtoBuilder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public ItemRequestDto build() {
            return new ItemRequestDto(id, description, created, userId);
        }
    }
}

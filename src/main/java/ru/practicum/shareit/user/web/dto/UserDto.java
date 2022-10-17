package ru.practicum.shareit.user.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User")
public class UserDto {
    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "User name", example = "Example", required = true)
    private String name;

    @Schema(description = "User e-mail", example = "example@email.com", required = true)
    @Email
    private String email;

    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public static class UserDtoBuilder {
        private Long id;
        private String name;
        @Email
        private String email;

        public UserDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDtoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserDtoBuilder email(@Email String email) {
            this.email = email;
            return this;
        }

        public UserDto build() {
            return new UserDto(id, name, email);
        }
    }
}

package ru.practicum.shareit.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User")
public class UserDto {
    @Schema(description = "User ID", example = "1")
    private Long id;

    @Schema(description = "User name", example = "Example", required = true)
    @NotBlank
    private String name;

    @Schema(description = "User e-mail", example = "example@email.com", required = true)
    @Email
    @NotBlank(message = "The email should not be null or blank.")
    private String email;
}

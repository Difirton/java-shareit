package ru.practicum.shareit.user.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Email;

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
    private String name;

    @Schema(description = "User e-mail", example = "example@email.com", required = true)
    @Email
    private String email;
}

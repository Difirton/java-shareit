package ru.practicum.shareit.item_request.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item request")
public class ItemRequestDto {
    @Schema(description = "Item request ID", example = "1")
    private Long id;

    @Schema(description = "Item request description", example = "example", required = true)
    @NotBlank
    private String description;

    @Schema(description = "Created date-time")
    private LocalDateTime created;

    @Schema(description = "User ID", example = "1")
    private Long userId;
}
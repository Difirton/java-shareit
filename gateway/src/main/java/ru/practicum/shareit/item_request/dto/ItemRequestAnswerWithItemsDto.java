package ru.practicum.shareit.item_request.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item request")
public class ItemRequestAnswerWithItemsDto {
    @Schema(description = "Item request ID", example = "1")
    private Long id;

    @Schema(description = "Item request description", example = "example", required = true)
    @NotBlank
    private String description;

    @Schema(description = "Created date-time")
    private LocalDateTime created;

    @Schema(description = "User ID", example = "1")
    private Long userId;

    @Schema(description = "Transferred items")
    private List<ItemDto> items;
}

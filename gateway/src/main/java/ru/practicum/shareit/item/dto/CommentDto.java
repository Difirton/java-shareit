package ru.practicum.shareit.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Comment")
public class CommentDto {
    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "Item ID", example = "1")
    private Long itemId;

    @Schema(description = "Comment text", example = "example", required = true)
    @NotBlank
    private String text;

    @Schema(description = "Author ID", example = "1")
    private Long authorId;

    @Schema(description = "Author name", example = "example")
    private String authorName;

    @Schema(description = "Item ID")
    @PastOrPresent
    private LocalDateTime created;
}

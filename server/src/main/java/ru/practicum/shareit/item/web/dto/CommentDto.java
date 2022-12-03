package ru.practicum.shareit.item.web.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;

    private Long itemId;

    @NotNull
    private String text;

    private Long authorId;

    @NotNull
    private String authorName;

    @PastOrPresent
    private LocalDateTime created;
}

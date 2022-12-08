package ru.practicum.shareit.item.web.dto;

import lombok.*;

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

    private String text;

    private Long authorId;

    private String authorName;

    private LocalDateTime created;
}

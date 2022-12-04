package ru.practicum.shareit.item.web.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ItemDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    @JsonIgnore
    private Long userId;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    private BookingItemDto nextBooking;

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    private BookingItemDto lastBooking;

    @Builder.Default
    @JsonProperty("comments")
    List<CommentDto> commentsDto = new ArrayList<>();

    @JsonProperty("requestId")
    private Long itemRequestId;
}

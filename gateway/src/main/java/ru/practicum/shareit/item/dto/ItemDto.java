package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Schema(description = "Item")
public class ItemDto {
    @Schema(description = "Item ID", example = "1")
    private Long id;

    @Schema(description = "Item name", example = "example", required = true)
    @NotBlank
    private String name;

    @Schema(description = "Item description", example = "example", required = true)
    @NotBlank
    private String description;

    @Schema(description = "Is item available", example = "true", required = true)
    @NotNull
    private Boolean available;

    @JsonIgnore
    private Long userId;

    @Schema(description = "Next booking")
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    private BookingItemDto nextBooking;

    @Schema(description = "Last booking")
    @JsonIdentityInfo(
            generator = ObjectIdGenerators.PropertyGenerator.class,
            property = "id")
    private BookingItemDto lastBooking;

    @Schema(description = "Comments")
    @Builder.Default
    @JsonProperty("comments")
    List<CommentDto> commentsDto = new ArrayList<>();

    @JsonProperty("requestId")
    private Long itemRequestId;
}

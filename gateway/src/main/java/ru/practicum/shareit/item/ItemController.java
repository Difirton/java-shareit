package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Tag(name = "The item API", description = "API for interacting with endpoints associated with items")
@RequestMapping(path = "/items", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {
    private final ItemClient itemClient;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

    @Operation(summary = "Creates a new user's items by user id, which is specified in header", tags = "The item API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The item was created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not valid parameters"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<Object> createItem(@Valid @RequestBody ItemDto itemDto,
                                      @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER)
                                      Long userId) {
        return itemClient.createItem(userId, itemDto);
    }

    @Operation(summary = "Get the user's items by user id, which is specified in header", tags = "The item API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the requested items",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Object> getItems(@Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId,
                                    @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                    @RequestParam(defaultValue = "10") @Positive int size) {
        return itemClient.findAllByUserId(userId, from, size);
    }

    @Operation(summary = "Get the item by it's id, which is specified in URL", tags = "The item API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the requested item",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Object> getItemById(@Parameter(description = "Item ID") @PathVariable("id") Long id,
                        @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return itemClient.findItemById(userId, id);
    }

    @Operation(summary = "Update the item by it's id, which is specified in URL", tags = "The item API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The item was updated",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "403", description = "Do not have update permission")
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Object> updateItemById(@PathVariable("id") Long itemId, @RequestBody ItemDto itemDto,
                           @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @Operation(summary = "Delete the item by his id, which is specified in URL", tags = "The item API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The item was removed",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "403", description = "Do not have delete permission")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteItem(@PathVariable("id") Long itemId,
                    @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        itemClient.deleteItem(userId, itemId);
    }

    @Operation(summary = "Get the desired item specified in the title or description with the requested text",
            tags = "The item API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the requested items",
                    content = {
                            @Content(mediaType = "application/json")
                    })
    })
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Object> getItemByNameAndDescription(@Parameter(description = "query") @RequestParam("text") String query,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "10") @Positive int size) {
        return itemClient.searchItemByText(query, from, size);
    }

    @Operation(summary = "Creates a new user's comment", tags = "The item API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The comment was created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CommentDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not valid parameters")
    })
    @PostMapping(path = "/{itemId}/comment", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto, @PathVariable("itemId") Long itemId,
                          @Parameter(description = "Booker ID") @RequestHeader(USER_REQUEST_HEADER) Long bookerId) {
        return itemClient.createComment(bookerId, itemId, commentDto);
    }
}
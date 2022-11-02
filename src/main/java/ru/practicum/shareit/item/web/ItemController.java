package ru.practicum.shareit.item.web;

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
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.repository.Comment;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.web.convertor.CommentDtoToCommentConverter;
import ru.practicum.shareit.item.web.convertor.CommentToCommentDtoConverter;
import ru.practicum.shareit.item.web.convertor.ItemDtoToItemConverter;
import ru.practicum.shareit.item.web.convertor.ItemToItemDtoConverter;
import ru.practicum.shareit.item.web.dto.CommentDto;
import ru.practicum.shareit.item.web.dto.ItemDto;
import ru.practicum.shareit.user.web.dto.UserDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "The item API", description = "API for interacting with endpoints associated with items")
@RequestMapping(path = "/items", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemController {
    private final ItemService itemService;
    private final ItemDtoToItemConverter itemDtoToItemConverter;
    private final ItemToItemDtoConverter itemToItemDtoConverter;
    private final CommentDtoToCommentConverter commentDtoToCommentConverter;
    private final CommentToCommentDtoConverter commentToCommentDtoConverter;
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
    ItemDto createItem(@RequestBody ItemDto itemDto,
                       @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        itemDto.setUserId(userId);
        Item newItem = itemService.save(itemDtoToItemConverter.convert(itemDto));
        return itemToItemDtoConverter.convert(newItem);
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
    List<ItemDto> getItems(@Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        List<Item> allItems = itemService.findAll(userId);
        return allItems.stream()
                .map(itemToItemDtoConverter::convert)
                .map(itemService::setBookings)
                .collect(Collectors.toList());
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
    ItemDto getItemById(@Parameter(description = "Item ID") @PathVariable("id") Long id,
                        @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        Item item = itemService.findById(id);
        if (item.getOwner().getId().equals(userId)) {
            return itemService.setBookings(itemToItemDtoConverter.convert(itemService.findById(id)));
        }
        return itemToItemDtoConverter.convert(itemService.findById(id));
    } //TODO ПЕРЕДЕЛАТЬ!!!

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
    ItemDto updateItemById(@PathVariable("id") Long id, @RequestBody ItemDto updateItemDto,
                           @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        updateItemDto.setUserId(userId);
        Item updatedItem = itemService.update(id, itemDtoToItemConverter.convert(updateItemDto));
        return itemToItemDtoConverter.convert(updatedItem);
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
    void deleteItem(@PathVariable("id") Long id,
                    @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        itemService.deleteById(id, userId);
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
    List<ItemDto> getItemByNameAndDescription(@Parameter(description = "query") @RequestParam("text") String query) {
        List<Item> allItems = itemService.findByParam(query);
        return allItems.stream()
                .map(itemToItemDtoConverter::convert)
                .collect(Collectors.toList());
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
    CommentDto createComment(@RequestBody CommentDto commentDto, @PathVariable("itemId") Long itemId,
                          @Parameter(description = "Booker ID") @RequestHeader(USER_REQUEST_HEADER) Long bookerId) {
        commentDto.setItemId(itemId);
        commentDto.setAuthorId(bookerId);
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = commentDtoToCommentConverter.convert(commentDto);
        return commentToCommentDtoConverter.convert(itemService.saveComment(comment));
    }
}

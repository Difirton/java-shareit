package ru.practicum.shareit.item_request.web;

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
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.item_request.service.ItemRequestService;
import ru.practicum.shareit.item_request.web.convertor.ItemRequestDtoToItemRequestConvertor;
import ru.practicum.shareit.item_request.web.convertor.ItemRequestToItemRequestDtoConvertor;
import ru.practicum.shareit.item_request.web.dto.ItemRequestDto;
import ru.practicum.shareit.user.web.dto.UserDto;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Tag(name = "The item request API", description = "API for interacting with endpoints associated with item request")
@RequestMapping(path = "/requests", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private final ItemRequestDtoToItemRequestConvertor itemRequestFromDtoConvertor;
    private final ItemRequestToItemRequestDtoConvertor itemRequestToDtoConvertor;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

    @Operation(summary = "Creates a new user's item request by user id, which is specified in header",
            tags = "The item request API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "The item request was created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not valid parameters"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ItemRequestDto createItemRequest(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                  @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        itemRequestDto.setUserId(userId);
        ItemRequest newItemRequest = itemRequestService.save(itemRequestFromDtoConvertor.convert(itemRequestDto));
        return itemRequestToDtoConvertor.convert(newItemRequest);
    }

    @Operation(summary = "Get the user's items requests by user id, which is specified in header",
            tags = "The item request API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the requested items requests",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<ItemRequestDto> getItemsRequests(@Parameter(description = "User ID")
                                          @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        List<ItemRequest> allItemsRequests = itemRequestService.findAll(userId);
        return allItemsRequests.stream()
                .map(itemRequestToDtoConvertor::convert)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Get the item request by it's id, which is specified in URL", tags = "The item request API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Returns the requested item request",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Item request not found")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemRequestDto getItemRequestById(@Parameter(description = "Item request ID") @PathVariable("id") Long id) {
        return itemRequestToDtoConvertor.convert(itemRequestService.findById(id));
    }

    @Operation(summary = "Update the item request by it's id, which is specified in URL", tags = "The item request API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The item request was updated",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Item request not found"),
            @ApiResponse(responseCode = "403", description = "Do not have update permission")
    })
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    ItemRequestDto updateItemRequestById(@PathVariable("id") Long id,
                           @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId,
                           @RequestBody ItemRequestDto updateItemRequestDto) {
        updateItemRequestDto.setUserId(userId);
        ItemRequest updatedItem = itemRequestService.update(id, itemRequestFromDtoConvertor
                .convert(updateItemRequestDto));
        return itemRequestToDtoConvertor.convert(updatedItem);
    }

    @Operation(summary = "Delete the item request by it's id, which is specified in URL", tags = "The item request API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The item request was removed",
                    content = {
                            @Content(mediaType = "application/json")
                    }),
            @ApiResponse(responseCode = "404", description = "Item request not found"),
            @ApiResponse(responseCode = "403", description = "Do not have delete permission")
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    void deleteItemRequest(@PathVariable("id") Long id,
                           @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        itemRequestService.deleteById(id, userId);
    }
}
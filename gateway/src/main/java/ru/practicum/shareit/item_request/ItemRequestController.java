package ru.practicum.shareit.item_request;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item_request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.Optional;

@Validated
@RestController
@RequiredArgsConstructor
@Tag(name = "The item request API", description = "API for interacting with endpoints associated with item request")
@RequestMapping(path = "/requests", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;
    private static final String USER_REQUEST_HEADER = "X-Sharer-User-Id";

    @Operation(summary = "Creates a new user's item request by user id, which is specified in header",
            tags = "The item request API")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "The item request was created",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDto.class))
                    }),
            @ApiResponse(responseCode = "400", description = "Not valid parameters"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Object> createItemRequest(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                             @Parameter(description = "User ID") @RequestHeader(USER_REQUEST_HEADER)
                                             Long userId) {
        return itemRequestClient.createItemRequest(userId, itemRequestDto);
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
    ResponseEntity<Object> getItemsRequests(@Parameter(description = "User ID")
                                          @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return itemRequestClient.findItemRequestsByUserId(userId);
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
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Object> getPageableItemsRequests(@Parameter(description = "User ID")
                                          @RequestHeader(USER_REQUEST_HEADER) Long userId,
                                                  @RequestParam Optional<Integer> from,
                                                  @RequestParam Optional<Integer> size) {
        return itemRequestClient.findAllItemRequest(userId, from.orElse(10), size.orElse(10));
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
    ResponseEntity<Object> getItemRequestById(@Parameter(description = "Request ID") @PathVariable("id") Long id,
                                      @RequestHeader(USER_REQUEST_HEADER) Long userId) {
        return itemRequestClient.findItemRequestById(userId, id);
    }
}
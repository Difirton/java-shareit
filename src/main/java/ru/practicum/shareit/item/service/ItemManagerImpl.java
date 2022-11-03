package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.repository.Comment;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.web.convertor.CommentDtoToCommentConverter;
import ru.practicum.shareit.item.web.convertor.CommentToCommentDtoConverter;
import ru.practicum.shareit.item.web.convertor.ItemDtoToItemConverter;
import ru.practicum.shareit.item.web.convertor.ItemToItemDtoConverter;
import ru.practicum.shareit.item.web.dto.BookingItemDto;
import ru.practicum.shareit.item.web.dto.CommentDto;
import ru.practicum.shareit.item.web.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemManagerImpl implements ItemManager {
    private final ItemService itemService;
    private final ItemToItemDtoConverter itemToItemDtoConverter;
    private final ItemDtoToItemConverter itemDtoToItemConverter;
    private final CommentDtoToCommentConverter commentDtoToCommentConverter;
    private final CommentToCommentDtoConverter commentToCommentDtoConverter;

    @Override
    public ItemDto save(ItemDto itemDto, Long userId) {
        itemDto.setUserId(userId);
        Item newItem = itemService.save(itemDtoToItemConverter.convert(itemDto));
        return itemToItemDtoConverter.convert(newItem);
    }

    @Override
    public List<ItemDto> findAll(Long userId) {
        List<Item> allItems = itemService.findAll(userId);
        return allItems.stream()
                .map(itemToItemDtoConverter::convert)
                .filter(Objects::nonNull)
                .map(this::setBookings)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto setBookings(ItemDto itemDto) {
        List<Booking> allForItem = itemService.findAllByItemId(itemDto.getId());
        LocalDateTime now = LocalDateTime.now();
        if (!allForItem.isEmpty()) {
            Booking last = allForItem.get(0);
            Booking next = allForItem.get(allForItem.size() - 1);
            log.info("setBookings(). now: {}, lastBooking.end: {}, nextBooking.start: {}",
                    now, last.getFinish(), next.getStart());
            if (last.getStatus() != Status.REJECTED && last.getStatus() != Status.CANCELED)
                itemDto.setLastBooking(new BookingItemDto(last.getId(), last.getRenter().getId()));
            if (next.getStatus() != Status.REJECTED && next.getStatus() != Status.CANCELED)
                itemDto.setNextBooking(new BookingItemDto(next.getId(), next.getRenter().getId()));
        }
        return itemDto;
    }

    @Override
    public ItemDto findById(Long id, Long userId) {
        Item item = itemService.findById(id);
        if (item.getOwner().getId().equals(userId)) {
            ItemDto itemDto = itemToItemDtoConverter.convert(itemService.findById(id));
            return this.setBookings(Objects.requireNonNull(itemDto));
        }
        return itemToItemDtoConverter.convert(itemService.findById(id));
    }

    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto updateItemDto) {
        updateItemDto.setUserId(userId);
        Item updatedItem = itemService.update(itemId, itemDtoToItemConverter.convert(updateItemDto));
        return itemToItemDtoConverter.convert(updatedItem);
    }

    @Override
    public void deleteById(Long itemId, Long userId) {
        itemService.deleteById(itemId, userId);
    }

    @Override
    public List<ItemDto> findItemByParam(String query) {
        List<Item> allItems = itemService.findByParam(query);
        return allItems.stream()
                .map(itemToItemDtoConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto saveComment(Long itemId, Long bookerId, CommentDto commentDto) {
        commentDto.setItemId(itemId);
        commentDto.setAuthorId(bookerId);
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = commentDtoToCommentConverter.convert(commentDto);
        return commentToCommentDtoConverter.convert(itemService.saveComment(comment));
    }
}

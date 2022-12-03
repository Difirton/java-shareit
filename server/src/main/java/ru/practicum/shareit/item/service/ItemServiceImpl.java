package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.error.ItemAuthenticationException;
import ru.practicum.shareit.item.error.ItemNotAvailableException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.item.repository.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.common.utill.NotNullPropertiesCopier;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService, NotNullPropertiesCopier<Item> {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Override
    public Item save(@Valid Item item) {
        userService.findById(item.getOwner().getId());
        return itemRepository.save(item);
    }

    @Override
    public List<Item> findAll(Long userId) {
        return itemRepository.findAllByOwnerIdOrderById(userId);
    }

    @Override
    public Item findById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        item.setComments(commentRepository.findAllByItemId(id));
        return item;
    }

    @Override
    public Item update(Long id, Item itemToUpdate) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        if (item.getOwner().getId().equals(itemToUpdate.getOwner().getId())) {
            this.copyNotNullProperties(itemToUpdate, item);
            return itemRepository.save(item);
        } else {
            throw new ItemAuthenticationException(id, itemToUpdate.getOwner().getId());
        }
    }

    @Override
    public void deleteById(Long id, Long userId) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        if (item.getOwner().getId().equals(userId)) {
            itemRepository.deleteById(id);
        } else {
            throw new ItemAuthenticationException(id,userId);
        }
    }

    @Override
    public List<Item> findByParam(String query) {
        return itemRepository.findAllByCriteria(query);
    }

    @Override
    public Item findAvailableById(Long id) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        if (item.getAvailable()) {
            return item;
        } else {
            throw new ItemNotAvailableException(id);
        }
    }

    @Override
    public Item findAvailableRenter(Long id, Long renterId) {
        Item item = itemRepository.findItemByIdWithCheckNotOwner(id, renterId)
                .orElseThrow(() -> new IllegalStateException("Owner can't rent his item"));
        if (item.getAvailable()) {
            return item;
        } else {
            throw new ItemNotAvailableException(id);
        }
    }

    @Override
    public Comment saveComment(@Valid Comment comment) {
        Booking booking = bookingRepository.findFirstByItemIdAndRenterIdAndStatusAndFinishBefore(
                comment.getItem().getId(), comment.getAuthor().getId(), Status.APPROVED, LocalDateTime.now())
                .orElseThrow(() -> new IllegalStateException("Author with id = "
                + comment.getAuthor().getId() + " did not rent Item with id = " + comment.getItem().getId()));
        comment.setAuthor(booking.getRenter());
        return commentRepository.save(comment);
    }

    @Override
    public List<Booking> findAllByItemId(Long id) {
        return bookingRepository.findAllByItemIdOrderByStart(id);
    }
}

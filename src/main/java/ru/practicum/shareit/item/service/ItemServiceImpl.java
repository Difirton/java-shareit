package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.error.ItemAuthenticationException;
import ru.practicum.shareit.item.error.ItemNotAvailableException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.web.dto.BookingItemDto;
import ru.practicum.shareit.item.web.dto.ItemDto;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.common.utill.NotNullPropertiesCopier;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService, NotNullPropertiesCopier<Item> {
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;


    @Override
    public Item save(@Valid Item item) {
        userService.findById(item.getOwner().getId());
        return itemRepository.save(item);
    }

    @Override
    public List<Item> findAll(Long userId) {
        return itemRepository.findAllByOwnerId(userId);
    }

    @Override
    public Item findById(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
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
            //TODO добавить логирование
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
            //TODO добавить логирование
            throw new ItemNotAvailableException(id);
        }
    }

    //TODO передалать это уродство

    @Override
    public ItemDto setBookings(ItemDto itemDto) {
        List<Booking> allForItem = bookingRepository.findAllByItemIdOrderByStart(itemDto.getId());
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
}

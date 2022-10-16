package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.error.ItemAuthenticationException;
import ru.practicum.shareit.item.error.ItemNotAvailableException;
import ru.practicum.shareit.item.error.ItemNotFoundException;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.utill.NotNullPropertiesCopier;

import javax.validation.Valid;
import java.util.List;

@Validated
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService, NotNullPropertiesCopier<Item> {
    private final ItemRepository itemRepository;
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
            throw new ItemNotAvailableException(id);
        }
    }
}

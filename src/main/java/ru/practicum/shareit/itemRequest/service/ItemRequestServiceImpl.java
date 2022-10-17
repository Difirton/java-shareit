package ru.practicum.shareit.itemRequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.utill.NotNullPropertiesCopier;
import ru.practicum.shareit.itemRequest.error.ItemRequestNotAvailableException;
import ru.practicum.shareit.itemRequest.error.ItemRequestNotFoundException;
import ru.practicum.shareit.itemRequest.repository.ItemRequest;
import ru.practicum.shareit.itemRequest.repository.ItemRequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService, NotNullPropertiesCopier<ItemRequest> {
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequest save(ItemRequest itemRequest) {
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public List<ItemRequest> findAll(Long userId) {
        return itemRequestRepository.findAllByUserId(userId);
    }

    @Override
    public ItemRequest findById(Long id) {
        return itemRequestRepository.findById(id).orElseThrow(() -> new ItemRequestNotFoundException(id));
    }

    @Override
    public ItemRequest update(Long id, ItemRequest updateItemRequest) {
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new ItemRequestNotFoundException(id));
        this.copyNotNullProperties(updateItemRequest, itemRequest);
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public void deleteById(Long id, Long userId) {
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new ItemRequestNotFoundException(id));
        if (itemRequest.getUser().getId().equals(userId)) {
            itemRequestRepository.deleteById(id);
        } else {
            throw new ItemRequestNotAvailableException(id);
        }
    }
}

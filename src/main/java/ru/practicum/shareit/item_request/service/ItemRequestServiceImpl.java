package ru.practicum.shareit.item_request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.utill.NotNullPropertiesCopier;
import ru.practicum.shareit.item_request.error.ItemRequestAuthenticationException;
import ru.practicum.shareit.item_request.error.ItemRequestNotFoundException;
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.item_request.repository.ItemRequestRepository;

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
        if (itemRequest.getUser().getId().equals(updateItemRequest.getUser().getId())) {
            this.copyNotNullProperties(updateItemRequest, itemRequest);
            return itemRequestRepository.save(itemRequest);
        } else {
            throw new ItemRequestAuthenticationException(itemRequest.getUser().getId());
        }
    }

    @Override
    public void deleteById(Long id, Long userId) {
        ItemRequest itemRequest = itemRequestRepository.findById(id)
                .orElseThrow(() -> new ItemRequestNotFoundException(id));
        if (itemRequest.getUser().getId().equals(userId)) {
            itemRequestRepository.deleteById(id);
        } else {
            throw new ItemRequestAuthenticationException(id);
        }
    }
}

package ru.practicum.shareit.itemRequest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.utill.NotNullPropertiesCopier;
import ru.practicum.shareit.itemRequest.repository.ItemRequest;
import ru.practicum.shareit.itemRequest.repository.ItemRequestRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService, NotNullPropertiesCopier<ItemRequest> {
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemRequest save(ItemRequest convert) {
        return null;
    }

    @Override
    public List<ItemRequest> findAll(Long userId) {
        return null;
    }

    @Override
    public ItemRequest findById(Long id) {
        return null;
    }

    @Override
    public ItemRequest update(Long id, ItemRequest convert) {
        return null;
    }

    @Override
    public void deleteById(Long id, Long userId) {

    }
}

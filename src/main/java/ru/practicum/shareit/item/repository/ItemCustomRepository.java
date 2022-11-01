package ru.practicum.shareit.item.repository;

import java.util.List;
import java.util.Optional;

public interface ItemCustomRepository {
    List<Item> findAllByCriteria(String criteria);

    Optional<Item> findItemByIdWithCheckNotOwner(Long id, Long ownerId);
}

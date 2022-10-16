package ru.practicum.shareit.item.repository;

import java.util.List;

public interface ItemCustomRepository {
    List<Item> findAllByCriteria(String criteria);
}

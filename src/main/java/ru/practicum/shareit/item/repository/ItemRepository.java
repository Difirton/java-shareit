package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemCustomRepository {
    List<Item> findAllByOwnerId(Long ownerId);
}

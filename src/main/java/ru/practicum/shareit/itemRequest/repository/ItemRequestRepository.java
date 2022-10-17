package ru.practicum.shareit.itemRequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByUserId(Long id);
}

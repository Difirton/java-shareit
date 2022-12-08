package ru.practicum.shareit.item_request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long>, ItemRequestCustomRepository {

    List<ItemRequest> findAllByUserIdOrderByCreatedDesc(Long id);

    @EntityGraph(value = "with-item")
    List<ItemRequest> findAllByIdIsNot(Long id, Pageable pageable);
}

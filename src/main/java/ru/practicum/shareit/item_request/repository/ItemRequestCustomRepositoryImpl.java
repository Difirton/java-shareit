package ru.practicum.shareit.item_request.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ItemRequestCustomRepositoryImpl implements ItemRequestCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ItemRequest> findWithItemAll() {
        return entityManager.createQuery("SELECT ir FROM ItemRequest ir ORDER BY ir.id", ItemRequest.class)
                .setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("with-item"))
                .getResultList();
    }

    @Override
    public ItemRequest findWithItemById(Long id) {
        return entityManager.createQuery("SELECT ir FROM ItemRequest ir WHERE ir.id = :id ORDER BY ir.id",
                        ItemRequest.class).setParameter("id", id)
                .setHint("javax.persistence.fetchgraph", entityManager.getEntityGraph("with-item"))
                .getSingleResult();
    }
}

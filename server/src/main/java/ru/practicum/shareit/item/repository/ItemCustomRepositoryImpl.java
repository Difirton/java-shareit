package ru.practicum.shareit.item.repository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
public class ItemCustomRepositoryImpl implements ItemCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Item> findAllByCriteria(String pattern) {
        if (StringUtils.isBlank(pattern)) {
            return List.of();
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
        Root<Item> item = criteria.from(Item.class);
        criteria.select(item).where(
            cb.and(
                cb.or(
                    cb.like(cb.upper(item.get("name")), "%" + pattern.toUpperCase() + "%"),
                    cb.like(cb.upper(item.get("description")), "%" + pattern.toUpperCase() + "%")
                    ),
                cb.equal(item.get("available"), true)
            )
        );
        return entityManager.createQuery(criteria).getResultList();
    }

    @Override
    public Optional<Item> findItemByIdWithCheckNotOwner(Long id, Long ownerId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> criteria = cb.createQuery(Item.class);
        Root<Item> item = criteria.from(Item.class);
        criteria.select(item).where(
                cb.and(
                        cb.equal(item.get("id"), id),
                        cb.notEqual(item.get("owner"), ownerId)
                )
        );
        return Optional.of(entityManager.createQuery(criteria).getSingleResult());
    }
}

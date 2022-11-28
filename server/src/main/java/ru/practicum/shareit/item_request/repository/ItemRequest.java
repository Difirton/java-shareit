package ru.practicum.shareit.item_request.repository;

import lombok.*;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.user.repository.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "with-item",
        attributeNodes = {
                @NamedAttributeNode(value = "items")
        }
)
@Entity
@Table(name = "items_requests")
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @Column(name = "created")
    private LocalDateTime created;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemRequest")
    private List<Item> items = new ArrayList<>();
}
package ru.practicum.shareit.item.repository;

import lombok.*;
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.user.repository.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "items")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(exclude = {"owner", "itemRequest"})
@ToString(exclude = {"owner", "itemRequest"})
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq")
    @SequenceGenerator(name = "items_seq")
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @NotBlank
    @Column(name = "description", nullable = false, length = 600)
    private String description;

    @NotNull
    @Column(name = "available", nullable = false)
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_request_id")
    private ItemRequest itemRequest;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
}
package ru.practicum.shareit.item.repository;

import lombok.*;
import ru.practicum.shareit.item_request.repository.ItemRequest;
import ru.practicum.shareit.user.repository.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(name = "itemWithUser",
        attributeNodes = @NamedAttributeNode("owner")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode(exclude = {"owner", "itemRequest"})
@ToString(exclude = {"owner", "itemRequest"})
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_seq")
    @SequenceGenerator(name = "items_seq")
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @Column(nullable = false)
    private String description;

    @NotNull
    @Column(nullable = false)
    private Boolean available;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToOne(fetch = FetchType.LAZY)
    private ItemRequest itemRequest;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    public static class ItemBuilder {
        private Long id;
        @NotBlank
        private String name;
        @NotBlank
        private String description;
        @NotNull
        private Boolean available;
        private User owner;
        private ItemRequest itemRequest;

        private List<Comment> comments = new ArrayList<>();

        private ItemBuilder() {
        }

        public ItemBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ItemBuilder name(@NotBlank String name) {
            this.name = name;
            return this;
        }

        public ItemBuilder description(@NotBlank String description) {
            this.description = description;
            return this;
        }

        public ItemBuilder available(@NotNull Boolean available) {
            this.available = available;
            return this;
        }

        public ItemBuilder owner(User owner) {
            this.owner = owner;
            return this;
        }

        public ItemBuilder itemRequest(ItemRequest itemRequest) {
            this.itemRequest = itemRequest;
            return this;
        }

        public ItemBuilder comments(List<Comment> comments) {
            this.comments = comments;
            return this;
        }

        public Item build() {
            return new Item(id, name, description, available, owner, itemRequest, comments);
        }
    }
}
package ru.practicum.shareit.user.repository;

import lombok.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = {"items", "itemRequests", "bookings"})
@ToString(exclude = {"items", "itemRequests", "bookings"})
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Item> items = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<ItemRequest> itemRequests = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "renter")
    private List<Booking> bookings = new ArrayList<>();

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {
        private Long id;
        @NotBlank
        private String name;
        @Email
        @NotBlank
        private String email;
        private List<Item> items;
        private List<ItemRequest> itemRequests;
        private List<Booking> bookings;

        private UserBuilder() { }

        public UserBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserBuilder name(@NotBlank String name) {
            this.name = name;
            return this;
        }

        public UserBuilder email(@Email @NotBlank String email) {
            this.email = email;
            return this;
        }

        public UserBuilder items(List<Item> items) {
            this.items = items;
            return this;
        }

        public UserBuilder itemRequests(List<ItemRequest> itemRequests) {
            this.itemRequests = itemRequests;
            return this;
        }

        public UserBuilder bookings(List<Booking> bookings) {
            this.bookings = bookings;
            return this;
        }

        public User build() {
            return new User(id, name, email, items, itemRequests, bookings);
        }
    }
}

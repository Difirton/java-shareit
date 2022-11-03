package ru.practicum.shareit.user.repository;

import lombok.*;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item_request.repository.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name", "email"})
@ToString(of = {"id", "name", "email"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq")
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String name;

    @Email
    @NotBlank
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Item> items = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<ItemRequest> itemRequests = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "renter")
    private List<Booking> bookings = new ArrayList<>();

    public void addItem(Item item) {
        this.items.add(item);
        item.setOwner(this);
    }

    public void addItemRequest(ItemRequest itemRequest) {
        this.itemRequests.add(itemRequest);
        itemRequest.setUser(this);
    }

    public void addBooking(Booking booking) {
        this.bookings.add(booking);
        booking.setRenter(this);
    }
}
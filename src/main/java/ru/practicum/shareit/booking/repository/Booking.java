package ru.practicum.shareit.booking.repository;

import lombok.*;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.user.repository.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(exclude = {"renter", "item"})
@ToString(exclude = {"renter", "item"})
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookings_seq")
    @SequenceGenerator(name = "bookings_seq")
    private Long id;

    private LocalDateTime start;

    private LocalDateTime finish;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id")
    private User renter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;
}

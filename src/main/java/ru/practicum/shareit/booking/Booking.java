package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.repository.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bookings_seq")
    @Column(name = "id", nullable = false)
    Long id;

    LocalDateTime start;

    LocalDateTime finish;

    @Enumerated(value = EnumType.STRING)
    Staus staus;

    @ManyToOne
    @JoinColumn(name = "renter_id")
    User renter;

    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;
}

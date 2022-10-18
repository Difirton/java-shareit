package ru.practicum.shareit.booking.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.repository.constant.Status;
import ru.practicum.shareit.item.repository.Item;
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
    Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "renter_id")
    User renter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    Item item;

    public static BookingBuilder builder() {
        return new BookingBuilder();
    }

    public static class BookingBuilder {
        private Long id;

        private LocalDateTime start;

        private LocalDateTime finish;
        private Status status;
        private User renter;
        private Item item;

        private BookingBuilder() {
        }

        public BookingBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public BookingBuilder start(LocalDateTime start) {
            this.start = start;
            return this;
        }

        public BookingBuilder finish(LocalDateTime finish) {
            this.finish = finish;
            return this;
        }

        public BookingBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public BookingBuilder renter(User renter) {
            this.renter = renter;
            return this;
        }

        public BookingBuilder item(Item item) {
            this.item = item;
            return this;
        }

        public Booking build() {
            return new Booking(id, start, finish, status, renter, item);
        }
    }
}

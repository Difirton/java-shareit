package ru.practicum.shareit.itemRequest.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.repository.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items_requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_requests_seq")
    @SequenceGenerator(name = "items_requests_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    private String description;

    private LocalDateTime created;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static ItemRequestBuilder builder() {
        return new ItemRequestBuilder();
    }

    public static class ItemRequestBuilder {
        private Long id;

        @NotBlank
        private String description;
        private LocalDateTime created;
        private User user;

        public ItemRequestBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ItemRequestBuilder description(@NotBlank String description) {
            this.description = description;
            return this;
        }

        public ItemRequestBuilder created(LocalDateTime created) {
            this.created = created;
            return this;
        }

        public ItemRequestBuilder user(User user) {
            this.user = user;
            return this;
        }

        public ItemRequest build() {
            return new ItemRequest(id, description, created, user);
        }
    }
}
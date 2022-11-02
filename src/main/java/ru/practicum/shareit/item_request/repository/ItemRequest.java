package ru.practicum.shareit.item_request.repository;

import lombok.*;
import ru.practicum.shareit.user.repository.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "items_requests")
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "items_requests_seq")
    @SequenceGenerator(name = "items_requests_seq")
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String description;

    private LocalDateTime created;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
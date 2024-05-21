package ru.project.BoardGames.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name = "collections")
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name="name", nullable = false)
    private String name;
    @ElementCollection(targetClass = Game.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "games_collection", joinColumns = @JoinColumn(name = "collection_id"))
    @Column(name="game", nullable = false)
    private Set<Game> games;
    @Column(name="user_id", nullable = false)
    private Long userId;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Collection(String name, Set<Game> games, Long userId) {
        this.name = name;
        this.games = games;
        this.userId = userId;
    }
}

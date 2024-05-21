package ru.project.BoardGames.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "rating", nullable = false)
    private Float rating;
    @Column(name = "text", nullable = true)
    private String text;
    @Column(name = "game_id", nullable = false)
    private Long gameId;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Review(Float rating, Long gameId, String username) {
        this.rating = rating;
        this.gameId = gameId;
        this.username = username;
    }

    public Review(Float rating, String text, Long gameId, String username ) {
        this.rating = rating;
        this.text = text;
        this.gameId = gameId;
        this.username = username;
    }

}

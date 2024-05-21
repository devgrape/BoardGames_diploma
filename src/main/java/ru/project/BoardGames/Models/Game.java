package ru.project.BoardGames.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name = "games")
public class Game implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "rating")
    private Float rating;
    @Column(name = "description")
    private String description;
    @Column(name = "filename")
    private String filename;

    @Column(name = "type", nullable = false)
    private Type type;

    @Column(name = "time")
    private String timeGame;
    @Column(name = "number_players")
    private String numberPlayers;
    @Column(name = "age_players")
    private String agePlayers;
    @Column(name = "categories")
    private Set<Category> categories;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Game(String name, Float rating, String description, Type type, String timeGame, String numberPlayers, String agePlayers, Set<Category> categories) {
        this.name = name;
        this.rating = rating;
        this.description = description;
        this.type = type;
        this.timeGame = timeGame;
        this.numberPlayers = numberPlayers;
        this.agePlayers = agePlayers;
        this.categories = categories;
    }

    public Game(Long id, String name, String price) {
        this.id = id;
        this.name = name;
    }
}

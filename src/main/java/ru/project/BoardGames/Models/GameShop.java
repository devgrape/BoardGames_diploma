package ru.project.BoardGames.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
@Table(name = "game_shops")
public class GameShop {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "game_id", nullable = false)
    private Long gameId;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private Integer price;
    @Column(name = "href", nullable = false)
    private String href;
    @Column(name = "shop", nullable = false)
    private String shop;

    public GameShop(Long gameId, String name, Integer price, String href, String shop) {
        this.gameId = gameId;
        this.name = name;
        this.price = price;
        this.href = href;
        this.shop = shop;
    }

}

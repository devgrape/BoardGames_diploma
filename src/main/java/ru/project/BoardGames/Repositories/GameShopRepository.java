package ru.project.BoardGames.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.BoardGames.Models.GameShop;

import java.util.List;

@Repository
public interface GameShopRepository extends JpaRepository<GameShop, Long> {
    List<GameShop> findGamesShopByGameId(Long gameId);
}

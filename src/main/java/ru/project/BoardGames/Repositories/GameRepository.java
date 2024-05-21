package ru.project.BoardGames.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.project.BoardGames.Models.Category;
import ru.project.BoardGames.Models.Game;
import ru.project.BoardGames.Models.Type;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Game findGameByName(String name);

    @Query("SELECT g FROM Game g WHERE g.name=?1 or g.rating>=?2 or g.type=?3 or g.categories=?4")
    List <Game> findGames(String name, Float rating, Type type, List<Category> categories);

    @Query("SELECT g FROM Game g WHERE g.rating>=?1 ORDER BY rating")
    List <Game> findGamesTop(Float rating);

//    @Query(nativeQuery = true, value = "SELECT * FROM games ORDER BY rating")
//    List<Game> findSortedGamesByRating();
}

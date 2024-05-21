package ru.project.BoardGames.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.project.BoardGames.Models.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewsByGameId(Long gameId);

    @Query("SELECT r FROM Review r WHERE r.username=?1 and r.gameId=?2")
    Review findReviewByUsernameAndByGameId(String username, Long gameId);

    @Query(value = "SELECT sum(rating) FROM Review WHERE gameId=?1")
    Float sumRatingByGameId(Long gameId);
}

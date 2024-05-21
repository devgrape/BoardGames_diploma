package ru.project.BoardGames.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.BoardGames.Models.Game;
import ru.project.BoardGames.Models.Review;
import ru.project.BoardGames.Models.Role;
import ru.project.BoardGames.Models.User;
import ru.project.BoardGames.Repositories.GameRepository;
import ru.project.BoardGames.Repositories.ReviewRepository;
import ru.project.BoardGames.Services.GameService;

import java.io.IOException;
import java.util.Map;

@Controller
public class ReviewController {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private GameService gameService;


    @GetMapping("/addReview/game/{id}")
    public String getPageAddReview(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        if (reviewRepository.findReviewByUsernameAndByGameId(user.getUsername(), id) != null)
            model.addAttribute("review", reviewRepository.findReviewByUsernameAndByGameId(user.getUsername(), id));
        model.addAttribute("game", gameService.getGame(id));
        model.addAttribute("reviews", reviewRepository.findReviewsByGameId(id));
        return "review";
    }

//    @PostMapping("/addRating/game/{id}")
//    public String addRating(Model model, @AuthenticationPrincipal User user, @PathVariable("id") Long id, @RequestParam Float rating) throws IOException {
//        Game game = gameService.getGame(id);
//        if (reviewRepository.findReviewByUsernameAndByGameId(user.getUsername(), id) != null) {
//            Review review = reviewRepository.findReviewByUsernameAndByGameId(user.getUsername(), id);
//            review.setRating(rating);
//            reviewRepository.save(review);
//        }
//        else {
//            Review review = new Review(rating, id, user.getUsername());
//            reviewRepository.save(review);
//        }
//        game.setRating(reviewRepository.sumRatingByGameId(id) / reviewRepository.findReviewsByGameId(id).size());
//        gameRepository.save(game);
//        model.addAttribute("game", game);
//        return "redirect:/game/{id}";
//    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/addReview/game/{id}")
    public String addReview(Model model, @AuthenticationPrincipal User user,  @PathVariable("id") Long id,@RequestParam(required = false) String text, @RequestParam Float rating) throws IOException {
        Game game = gameService.getGame(id);
        if (reviewRepository.findReviewByUsernameAndByGameId(user.getUsername(), id) != null) {
            Review review = reviewRepository.findReviewByUsernameAndByGameId(user.getUsername(), id);
            if (text != "" || text != null) {
                review.setText(text);
            }
            review.setRating(rating);
//            review.setText(text);
            reviewRepository.save(review);
            model.addAttribute("review", review);
        }
        else {
            Review review = new Review(rating, text, id, user.getUsername());
            reviewRepository.save(review);
            model.addAttribute("review", review);
        }
        game.setRating(reviewRepository.sumRatingByGameId(id) / reviewRepository.findReviewsByGameId(id).size());
        gameRepository.save(game);
        model.addAttribute("game", game);
//        return "redirect:/game/" + game.getId().toString();
        return "/redirect:/game/" + id;
    }



}

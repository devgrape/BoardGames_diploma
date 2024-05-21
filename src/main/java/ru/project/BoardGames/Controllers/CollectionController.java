package ru.project.BoardGames.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.project.BoardGames.Models.Collection;
import ru.project.BoardGames.Models.Game;
import ru.project.BoardGames.Models.Review;
import ru.project.BoardGames.Models.User;
import ru.project.BoardGames.Repositories.CollectionRepository;
import ru.project.BoardGames.Repositories.GameShopRepository;
import ru.project.BoardGames.Services.GameService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class CollectionController {

    @Autowired
    private GameShopRepository gameShopRepository;

    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private GameService gameService;

    @GetMapping("/collection")
    public String getCollection(Model model, @AuthenticationPrincipal User user) {
        if (user  != null)
            model.addAttribute("user", user);
        if (collectionRepository.findCollectionByName("Favorite") == null) {
            Set<Game> games = new HashSet<Game>();
            Collection favoriteCollection = new Collection("Favorite", games, user.getId());
            collectionRepository.save(favoriteCollection);
        }
        model.addAttribute("collection", collectionRepository.findCollectionByName("Favorite"));
        model.addAttribute("games", collectionRepository.findCollectionByName("Favorite").getGames());
        return "index";
    }

    @PostMapping("/addToMyCollection/game/{id}")
    public String addToMyCollection(Model model, @AuthenticationPrincipal User user, @PathVariable("id") Long id) throws IOException {
        Game game = gameService.getGame(id);
        if (collectionRepository.findCollectionByName("Favorite") != null) {
            Collection favoriteCollection = collectionRepository.findCollectionByName("Favorite");
            favoriteCollection.getGames().add(game);
            collectionRepository.save(favoriteCollection);
        }
        else {
            Set<Game> games = new HashSet<Game>();
            games.add(game);
            Collection favoriteCollection = new Collection("Favorite", games, user.getId());
            collectionRepository.save(favoriteCollection);
        }
        return "redirect:/collection";
    }

}

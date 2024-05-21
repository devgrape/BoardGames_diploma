package ru.project.BoardGames.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.project.BoardGames.Models.Category;
import ru.project.BoardGames.Models.Game;
import ru.project.BoardGames.Models.Type;
import ru.project.BoardGames.Models.User;
import ru.project.BoardGames.Repositories.GameRepository;
import ru.project.BoardGames.Repositories.GameShopRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.lang.System.getProperty;
import static ru.project.BoardGames.Services.GameShopService.*;
import static ru.project.BoardGames.Services.GameShopService.getGameHobby;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameRepository ratingRepository;

    @Autowired
    private GameShopRepository gameShopRepository;

    @Value("${data.path}")
    private String uploadPath;

    public List<Game> getGames() {
        return gameRepository.findAll();
    }

//    @Cacheable(value = "games", key = "#id")
    public Game getGame(Long id) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        Game game = optionalGame.orElse(null);
        return game;
    }


    public List <Game> getGamesTop() {
        List <Game> games = gameRepository.findGamesTop(7.0F);
        return games;
    }

    public Game getGameByName(String name) {
        Game game = gameRepository.findGameByName(name);
        return game;
    }

    public List <Game> getGamesByParameters(String name, Float rating, Type type, List <Category> categories) {
        List<Game> games = gameRepository.findGames(name, rating, type, categories);
        return games;
    }


    public void deleteGame(Long id) {
        gameRepository.deleteById(id);
    }

    public Game addGame(String name, String description, MultipartFile file, Type type,
                          String timeGame, String numberPlayers, String agePlayers,
                          Set<Category> categories) throws IOException {
        timeGame = timeGame + " минут";
        if (gameRepository.findGameByName(name) != null) {
            return null;
        }
        Float rating = 0.0F;
        Game game = new Game(name, rating, description, type, timeGame, numberPlayers, agePlayers, categories);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(Paths.get(uploadPath).toAbsolutePath().normalize().toString().replace("\\", "/"));
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultImage = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(Paths.get(uploadPath).toAbsolutePath().normalize().toString().replace("\\", "/") + "/" + resultImage));
            game.setFilename(resultImage);
        }
        gameRepository.save(game);
        if (getGameYandexMarket(game.getId(), game.getName()) != null)
            gameShopRepository.save(getGameYandexMarket(game.getId(), game.getName()));
        if (getGameLavkaIgr(game.getId(), game.getName()) != null)
            gameShopRepository.save(getGameLavkaIgr(game.getId(), game.getName()));
        if (getGameHobby(game.getId(), game.getName()) != null)
            gameShopRepository.save(getGameHobby(game.getId(), game.getName()));
        return game;
//        model.addAttribute("game", game);
//        return "redirect:/game/" + game.getId().toString();
    }




}

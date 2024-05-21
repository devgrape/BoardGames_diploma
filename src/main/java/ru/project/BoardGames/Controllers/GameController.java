package ru.project.BoardGames.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.project.BoardGames.Models.*;
import ru.project.BoardGames.Repositories.GameShopRepository;
import ru.project.BoardGames.Services.FriendService;
import ru.project.BoardGames.Services.GameService;
import ru.project.BoardGames.Services.UserService;


import java.io.IOException;

import java.util.*;



@Controller
public class GameController {

    @Value("${data.path}")
    private String uploadPath;

    @Autowired
    private GameShopRepository gameShopRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;


    //    @Cacheable(value="Games")
    @GetMapping("/")
    public String getGames(Model model, @AuthenticationPrincipal User user){
        if (user != null)
            model.addAttribute("user", user);
        model.addAttribute("games", gameService.getGames());
        return "index";
    }

    //    @Cacheable(value="Games")
    @GetMapping("/gamesTop")
    public String getGamesTop(Model model, @AuthenticationPrincipal User user){
        if (user != null)
            model.addAttribute("user", user);
        model.addAttribute("games", gameService.getGamesTop());
        return "index";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/addGame")
    public String getPageAddGame(Model model, @AuthenticationPrincipal User user){
        if (user != null)
            model.addAttribute("user", user);
        return "game_add";
    }



    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/addGame")
    public String addGame(@RequestParam String name,
                      @RequestParam String description,
                      @RequestParam("file") MultipartFile file,
                      @RequestParam Type type,
                      @RequestParam String timeGame,
                      @RequestParam String numberPlayers,
                      @RequestParam String agePlayers,
                      @RequestParam Set<Category> categories,
                      Model model
                      ) throws IOException {
        Game game = gameService.addGame(name, description, file, type, timeGame, numberPlayers, agePlayers, categories);
        if (game == null) {
            model.addAttribute("message", "Игра с таким именем уже существует!");
            return "game_add";
        }
        model.addAttribute("game", game);
        return "redirect:/game/" + game.getId();
    }


//    @Cacheable(value="Game", key="#id")
    @RequestMapping("/game/{id}")
    public String getGame(Model model, @AuthenticationPrincipal User user, @PathVariable("id") Long id) throws NullPointerException, IOException {
        if (user != null)
            model.addAttribute("user", user);
        model.addAttribute("game", gameService.getGame(id));
        model.addAttribute("gameShops", gameShopRepository.findGamesShopByGameId(id));
        return "game";
    }


    @DeleteMapping("/game/{id}")
//    @CacheEvict(value="Game", key="#id")
    public void deleteGame(@PathVariable("id") Long id) {
        gameService.deleteGame(id);
    }

    @GetMapping("/search")
    public String searchGame(Model model, @RequestParam String keyword, @AuthenticationPrincipal User user) {
        if (user != null)
            model.addAttribute("user", user);
        if (gameService.getGameByName(keyword) != null) {
            model.addAttribute("game", gameService.getGameByName(keyword));
            return "redirect:/game" + gameService.getGameByName(keyword).getId();
        }
        if (userService.getUserByName(keyword) != null) {
            return "redirect:/profile/" + keyword;
        }
        return "search_game";
    }

    @GetMapping("/searchGame")
    public String main(Model model, @AuthenticationPrincipal User user) {
        if (user != null)
            model.addAttribute("user", user);
        model.addAttribute("game", new Game());
        return "search_game";
    }

    @PostMapping("/searchGame")
    public String searchGamesByParameters(@RequestParam(required = false) String name,
                       @RequestParam(required = false) Float rating,
                       @RequestParam(required = false) Type type,
                       @RequestParam(required = false) List <Category> categories,
    Model model) {
        List <Game> games = gameService.getGamesByParameters(name, rating, type, categories);
        if (games.isEmpty()) {
            model.addAttribute("message", "К сожалению, ничего не найдено");
            return "error";
        }
        model.addAttribute("games", games);
        return "index";
    }



}

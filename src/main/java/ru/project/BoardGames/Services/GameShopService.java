package ru.project.BoardGames.Services;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.BoardGames.Models.GameShop;
import ru.project.BoardGames.Repositories.GameShopRepository;

import java.io.IOException;
import java.util.List;


@Service
public class GameShopService {
    @Autowired
    GameShopRepository gameShopRepository;
    public static double findSimilarity(String x, String y) {

        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) {

            return (maxLength - StringUtils.getLevenshteinDistance(x, y)) / maxLength;
        }
        return 1.0;
    }
    public static GameShop getGameHobby(Long gameId, String nameGame) throws IOException {
        Document doc = Jsoup.connect("https://hobbygames.ru/catalog/search?keyword=" + nameGame).get();
        for (Element name : doc.select("a.name")) {
            for (Element priceFrom : doc.select("span.price")) {
                String price = priceFrom.text().replaceAll("\\D+", "");
                if (nameGame.equals(name.text())) {
                    GameShop gameHobby = new GameShop(gameId, name.text(), Integer.parseInt(price), name.attr("href"), "Hobby Games");
                    return gameHobby;

                }
            }
        }
        return null;
    }

    public static GameShop getGameLavkaIgr(Long gameId, String nameGame) throws IOException {
        Document doc = Jsoup.connect("https://www.lavkaigr.ru/shop/search/?query=" + nameGame).get();
        for (Element name : doc.select("a.game-name")) {
            for (Element priceFrom : doc.select("p.price")) {
                String price = priceFrom.text().replaceAll("\\D+", "");
                if (nameGame.equals(name.text())) {
                    GameShop gameLavkaIgr = new GameShop(gameId, name.text(), Integer.parseInt(price), name.attr("href"), "Лавка игр");
                    return gameLavkaIgr;

                }
            }
        }
        return null;
    }

    public static GameShop getGameYandexMarket(Long gameId, String nameGame) throws IOException {
        Document doc = Jsoup.connect("https://market.yandex.ru/search?text=" + nameGame).get();
        double maxSimularity = 0;
        Element elSimularity = null;
        String priceSimularity = null;
        for (Element el : doc.select("a.EQlfk")) {
            for (Element priceFrom : doc.select("span._1ArMm")) {
                String price = priceFrom.text().replaceAll("\\D+", "");
                if (nameGame.equals(el.select("> h3").text())) {
                    GameShop gameYandexMarket = new GameShop(gameId, el.select("> h3").text(), Integer.parseInt(price), "https://market.yandex.ru" + el.attr("href"), "Яндекс Маркет");
                    return gameYandexMarket;

                }
                if (maxSimularity < findSimilarity(el.select("> h3").text(), nameGame)) {
                    maxSimularity = findSimilarity(el.select("> h3").text(), nameGame);
                    elSimularity = el;
                    priceSimularity = price;
                }
            }
        }
        if (maxSimularity > 0.75) {
            GameShop gameYandexMarket = new GameShop(gameId, elSimularity.select("> h3").text(), Integer.parseInt(priceSimularity), "https://market.yandex.ru" + elSimularity.attr("href"), "Яндекс Маркет");
            return gameYandexMarket;
        }
        return null;
    }

    public List<GameShop> findGameShopByGameId(Long gameId) {
        return gameShopRepository.findGamesShopByGameId(gameId);
    }
}


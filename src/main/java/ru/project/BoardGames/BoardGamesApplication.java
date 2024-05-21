package ru.project.BoardGames;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;




@SpringBootApplication

@EnableWebMvc
//@EnableCaching
public class BoardGamesApplication {
	public static void main(String[] args)  {
		SpringApplication.run(BoardGamesApplication.class, args);
	}

}

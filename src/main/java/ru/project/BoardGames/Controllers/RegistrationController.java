package ru.project.BoardGames.Controllers;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.project.BoardGames.Models.Role;
import ru.project.BoardGames.Models.User;
import ru.project.BoardGames.Repositories.UserRepository;
import ru.project.BoardGames.Services.UserService;

import java.util.*;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        User userFromDb = userRepository.findUserByUsername(user.getUsername());
        if (userFromDb != null) {
            model.addAttribute("message", "Данный пользователь уже существует");
            return "registration";
        }
        User userFromDb1 = userRepository.findUserByEmail(user.getEmail());
        if (userFromDb1 != null){
            model.addAttribute("message", "Данный пользователь уже существует");
            return "registration";
        }
        user.setFilename("nophoto.png");
        user.setRoles(Collections.singleton(Role.USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setPrivacy(true);
        userRepository.save(user);
        model.addAttribute("messageOfLogin", "Регистрация прошла успешно");
        return "redirect:/login";
    }


}

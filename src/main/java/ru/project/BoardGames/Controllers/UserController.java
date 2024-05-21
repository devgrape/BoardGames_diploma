package ru.project.BoardGames.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import ru.project.BoardGames.Models.Role;
import ru.project.BoardGames.Models.User;
import ru.project.BoardGames.Repositories.UserRepository;
import ru.project.BoardGames.Services.UserService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${data.path}")
    private String uploadPath;


    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/settings/setUserImage")
    public String setUserImage(@AuthenticationPrincipal User user,
                               @RequestParam("file") MultipartFile file) throws IOException {
        userService.setUserImage(user, file);
        return "userProfile";
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/settings/setPrivacy")
    public String setPrivacy(@AuthenticationPrincipal User user, @RequestParam(value = "privacy", required = false) boolean privacy, Model model) throws IOException {
        if (user != null)
            model.addAttribute("user", user);
        userService.setPrivacy(user, privacy);
        return "redirect:/profile";
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/settings")
    public String getSettings(Model model, @AuthenticationPrincipal User user) {
        if (user != null)
            model.addAttribute("user", user);
        return "userSettings";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/settings")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String newPassword,
            @RequestParam String oldPassword,
            @RequestParam(required = false) String username,
            Model model) {
        String messageP = userService.updateUser(user, newPassword, oldPassword, username);
        model.addAttribute("messageP", messageP);
        model.addAttribute("user", user);
        return "userSettings";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin")
    public String getAdmin(Model model, @AuthenticationPrincipal User user){
        if (user != null)
            model.addAttribute("user", user);
        return "admin";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/manageUsers")
    public String getPageManageUsers(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getUsers());
        return "manageUsers";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/manageUser/user/{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/admin/manageUser/user/{user}")
    public String userSave(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user) {
        userService.addUser(user, username, email, form);
        return "redirect:/admin/manageUsers";
    }



}

package ru.project.BoardGames.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.project.BoardGames.Models.Game;
import ru.project.BoardGames.Models.Role;
import ru.project.BoardGames.Repositories.UserRepository;
import ru.project.BoardGames.Models.User;
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${data.path}")
    private String uploadPath;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserByName(String name) {
        return userRepository.findUserByUsername(name);
    }

    public User getUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user = optionalUser.orElse(null);
        return user;
    }


    @Override
    public UserDetails loadUserByUsername(String data) throws UsernameNotFoundException {
        User userByUsername = userRepository.findUserByUsername(data);
        User userByEmail = userRepository.findUserByEmail(data);
        if (userByEmail == null) {
            if (userByUsername == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return userByUsername;
        }
        return userByEmail;
    }


    public void addUser(User user, String username, String email, Map<String, String> form) {
        user.setUsername(username);
        user.setEmail(email);
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
        userRepository.save(user);
    }

    public String updateUser(User user, String newPassword, String oldPassword, String username) {
        if (user.getPassword().equals(oldPassword)) {
            if (!username.isEmpty())
                if (userRepository.findUserByUsername(username) == null || user.getUsername().equals(username))
                    user.setUsername(username);
                else return "Данный username уже используется";
            if (!newPassword.isEmpty())
                user.setPassword(newPassword);
            userRepository.save(user);
            return "Данные успешно изменены";
        }
        return "Неверный пароль, попробуйте еще раз";
    }


    public void setUserImage(@AuthenticationPrincipal User user,
                               @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(Paths.get(uploadPath).toAbsolutePath().normalize().toString().replace("\\", "/"));
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String uuidFile = UUID.randomUUID().toString();
            String resultImage = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(Paths.get(uploadPath).toAbsolutePath().normalize().toString().replace("\\", "/") + "/" + resultImage));
            user.setFilename(resultImage);
        }
        userRepository.save(user);
    }

    public void setPrivacy(User user, boolean privacy) throws IOException {
        user.setPrivacy(privacy);
        userRepository.save(user);
    }

//    public void deleteUser(Long id){
//        userRepository.deleteById(id);
//    }
}


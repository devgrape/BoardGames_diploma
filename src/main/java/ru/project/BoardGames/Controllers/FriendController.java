package ru.project.BoardGames.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import ru.project.BoardGames.Models.User;
import ru.project.BoardGames.Services.FriendService;
import ru.project.BoardGames.Services.UserService;


@Controller
public class FriendController {

    @Autowired
    FriendService friendService;
    @Autowired
    UserService userService;


    @GetMapping("/profile/{username}")
    public String getProfile(Model model, @AuthenticationPrincipal User user, @PathVariable("username") String username) {
        if (user != null)
            model.addAttribute("user", user);
        User searchUser = userService.getUserByName(username);
        model.addAttribute("searchUser", searchUser);
        model.addAttribute("numberOfFriends", friendService.getNumberOfFriends(searchUser.getId()));
        model.addAttribute("numberOfFollowers", friendService.getNumberOfFollowers(searchUser.getId()));
        if (user != null && !user.equals(searchUser))
            model.addAttribute("friend", friendService.getFriendByUserIdAndFriendId(user.getId(), searchUser.getId()));
        return "profile";
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/friends")
    public String getFriends(Model model, @AuthenticationPrincipal User user) {
        if (user != null)
            model.addAttribute("user", user);
        model.addAttribute("friends", friendService.getFriends(user.getId()));
        model.addAttribute("title1", "Мои друзья");
        return "friends";
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/followers")
    public String getFollowers(Model model, @AuthenticationPrincipal User user) {
        if (user != null)
            model.addAttribute("user", user);
        model.addAttribute("friends", friendService.getFollowers(user.getId()));
        model.addAttribute("title1", "Мои подписчики");
        return "friends";
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/subscriptions")
    public String getSubscriptions(Model model, @AuthenticationPrincipal User user) {
        if (user != null)
            model.addAttribute("user", user);
        model.addAttribute("friends", friendService.getSubscriptions(user.getId()));
        model.addAttribute("title1", "Мои подписки");
        return "friends";
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/requests")
    public String getNotApprovedFriends(Model model, @AuthenticationPrincipal User user) {
        if (user != null)
            model.addAttribute("user", user);
        model.addAttribute("friends", friendService.getNotApprovedFriends(user.getId()));
        model.addAttribute("title1", "Входящие заявки в друзья");
        return "friends";
    }
    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/myRequests")
    public String getMyRequests(Model model, @AuthenticationPrincipal User user) {
        if (user != null)
            model.addAttribute("user", user);
        model.addAttribute("friends", friendService.getRequests(user.getId()));
        model.addAttribute("title1", "Исходящие заявки в друзья");
        return "friends";
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/user/addToFriend/{id}")
    public String addFriend( @AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model){
        friendService.addFriend(user.getId(), id);
        return "redirect:/myRequests";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/user/accept/{id}")
    public String acceptFriend( @AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model){
        friendService.acceptFriend(user.getId(), id);
        model.addAttribute("friends", friendService.getFriends(user.getId()));
        model.addAttribute("title1", "Мои друзья");
        return "redirect:/friends";
    }
    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/user/decline/{id}")
    public String declineFriend( @AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model){
        friendService.declineFriend(user.getId(), id);
        return "redirect:/friends";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/user/unfriended/{id}")
    public String unfriended( @AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model){
        friendService.deleteFriend(user.getId(), id);
        model.addAttribute("friends", friendService.getFollowers(user.getId()));
        model.addAttribute("title1", "Мои подписчики");
        return "redirect:/followers";
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/user/subscribe/{id}")
    public String subscribe( @AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model){
        friendService.subscribe(user.getId(), id);
        return "redirect:/subscriptions";
    }
    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/user/unsubscribe/{id}")
    public String unsubscribe( @AuthenticationPrincipal User user, @PathVariable("id") Long id, Model model){
        friendService.unsubscribe(user.getId(), id);
        model.addAttribute("friends", friendService.getSubscriptions(user.getId()));
        model.addAttribute("title1", "Мои подписки");
        return "redirect:/subscriptions";
    }

}

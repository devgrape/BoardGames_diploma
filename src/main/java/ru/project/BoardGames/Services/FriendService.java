package ru.project.BoardGames.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.project.BoardGames.Models.Friend;
import ru.project.BoardGames.Models.Relation;
import ru.project.BoardGames.Models.Role;
import ru.project.BoardGames.Models.User;
import ru.project.BoardGames.Repositories.FriendRepository;
import ru.project.BoardGames.Services.UserService;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class FriendService {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserService userService;

    public Set<User> getFriends(Long userId){
        Set <Friend> friends = friendRepository.findFriends(userId, Relation.FRIEND);
        Set <User> users = new HashSet<User>();
        for (Friend friend : friends) {
            if (friend.getUserId() == userId) {
                users.add(userService.getUser(friend.getUserId()));
            }
            if (friend.getFriendId() == userId)
                users.add(userService.getUser(friend.getFriendId()));
        }
        return users;
    }
    public int getNumberOfFriends(Long userId){
        return friendRepository.findFriends(userId,Relation.FRIEND).size();
    }
    public int getNumberOfFollowers(Long userId){
        int num = 0;
        Set <Friend> followers = friendRepository.findFollowers(userId, Relation.FOLLOWER);
        for (Friend follower : followers) {
            if (follower.getFriendId() == userId) {
                num++;
            }
        }
        return num;
    }

    public Set<User> getFollowers(Long userId){
        Set <Friend> followers = friendRepository.findFollowers(userId, Relation.FOLLOWER);
        Set <User> users = new HashSet<User>();
        for (Friend follower : followers) {
            if (follower.getFriendId() == userId) {
                users.add(userService.getUser(follower.getFriendId()));
            }
        }
        return users;
    }

    public Set<User> getNotApprovedFriends(Long userId){
        Set <Friend> friends = friendRepository.findNotApprovedFriend(userId, Relation.NOT_APPROVED_FRIEND);
        Set <User> users = new HashSet<User>();
        for (Friend friend : friends) {
            if (friend.getFriendId() == userId) {
                users.add(userService.getUser(friend.getFriendId()));
            }
        }
        return users;
    }

    public Set<User> getSubscriptions(Long userId){
        Set <Friend> friends = friendRepository.findSubscriptions(userId, Relation.FOLLOWER);
        Set <User> users = new HashSet<User>();
        for (Friend friend : friends) {
            if (friend.getUserId() == userId) {
                users.add(userService.getUser(friend.getUserId()));
            }
        }
        return users;
    }

    public Set<User> getRequests(Long userId){
        Set <Friend> friends = friendRepository.findRequests(userId, Relation.NOT_APPROVED_FRIEND);
        Set <User> users = new HashSet<User>();
        for (Friend friend : friends) {
            if (friend.getUserId() == userId) {
                users.add(userService.getUser(friend.getFriendId()));
            }
        }
        return users;

    }

    public void addFriend(Long userId, Long friendId) {
        if (friendRepository.findRelationsByUserIdAndByFriendId(userId, friendId) != null) {
            return;
        }
        Friend friend = new Friend(userId, friendId, Relation.NOT_APPROVED_FRIEND);
        friendRepository.save(friend);
    }

    public void acceptFriend(Long userId, Long friendId) {
        Friend friend = friendRepository.findRelationsByUserIdAndByFriendId(userId, friendId);
        if (friend != null && friend.getRelationType() == Relation.NOT_APPROVED_FRIEND) {
            friend.setRelationType(Relation.FRIEND);
            friendRepository.save(friend);
        }
        return;
    }
    public void declineFriend(Long userId, Long friendId) {
        Friend friend = friendRepository.findRelationsByUserIdAndByFriendId(userId, friendId);
        if (friend != null && friend.getRelationType() == Relation.NOT_APPROVED_FRIEND) {
            friend.setUserId(friendId);
            friend.setRelationType(Relation.DECLINED_FRIEND);
            friend.setFriendId(userId);
            friendRepository.save(friend);
        }
    }
    public void subscribe(Long userId, Long friendId) {
        if (friendRepository.findRelationsByUserIdAndByFriendId(userId, friendId) != null)
            return;
        Friend friend = new Friend(userId, friendId, Relation.FOLLOWER);
        friendRepository.save(friend);
    }
    public void deleteFriend(Long userId, Long friendId) {
       Friend friend = friendRepository.findRelationsByUserIdAndByFriendId(userId, friendId);
       if (friend == null && friend.getRelationType() != Relation.FRIEND)
           return;
       friend.setUserId(friendId);
       friend.setFriendId(userId);
       friend.setRelationType(Relation.FOLLOWER);
       friendRepository.save(friend);
    }
    public void unsubscribe(Long userId, Long friendId) {
        Friend friend = friendRepository.findRelationsByUserIdAndByFriendId(userId, friendId);
        if (friend == null && (friend.getRelationType() != Relation.FOLLOWER))
            return;
        friendRepository.delete(friend);
    }

    public Friend getFriendByUserIdAndFriendId(Long userId, Long friendId){
        return friendRepository.findRelationsByUserIdAndByFriendId(userId, friendId);
    }

}

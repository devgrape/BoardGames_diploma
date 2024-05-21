package ru.project.BoardGames.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.project.BoardGames.Models.Friend;
import ru.project.BoardGames.Models.Relation;
import java.util.Set;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

    @Query("SELECT f FROM Friend f WHERE (f.userId=?1 or f.friendId=?1) and f.relationType=?2")
    Set<Friend> findFriends(Long userId, Relation relationType);

    @Query("SELECT f FROM Friend f WHERE f.friendId=?1 and f.relationType=?2")
    Set<Friend> findFollowers(Long userId, Relation relationType); // пользователи, которые подписаны на текущего пользователя

    @Query("SELECT f FROM Friend f WHERE f.friendId=?1 and f.relationType=?2")
    Set<Friend> findNotApprovedFriend(Long userId, Relation relationType); // пользователи, которые отправили заявку в друзья

    @Query("SELECT f FROM Friend f WHERE f.userId=?1 and f.relationType=?2")
    Set<Friend> findSubscriptions(Long userId, Relation relationType); // пользователи, на которых подписан текущий

    @Query("SELECT f FROM Friend f WHERE f.userId=?1 and f.relationType=?2")
    Set<Friend> findRequests(Long userId, Relation relationType); // пользователи, которым отправил заявку в друзья текущий

    @Query("SELECT f FROM Friend f WHERE (f.userId=?1 or f.friendId=?2) or (f.userId=?2 or f.friendId=?1)")
    Friend findRelationsByUserIdAndByFriendId(Long userId, Long friendId);

}

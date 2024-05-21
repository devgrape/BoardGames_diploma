package ru.project.BoardGames.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "friends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name="friend_id", nullable = false)
    private Long friendId;

    @Column(name = "relation_type", nullable = false)
    private Relation relationType;


    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public Friend(Long userId, Long friendId, Relation relationType) {
        this.userId = userId;
        this.friendId = friendId;
        this.relationType = relationType;
    }
}
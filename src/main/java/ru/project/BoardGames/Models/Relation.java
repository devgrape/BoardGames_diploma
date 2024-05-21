package ru.project.BoardGames.Models;

import org.springframework.security.core.GrantedAuthority;

public enum Relation {
    FRIEND, NOT_APPROVED_FRIEND, DECLINED_FRIEND, FOLLOWER;
}
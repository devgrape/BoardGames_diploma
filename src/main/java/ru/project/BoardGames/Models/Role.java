package ru.project.BoardGames.Models;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER("пользователь"), ADMIN("администратор"), USER_BANNED("заблокированный пользователь");
    private final String displayValue;

    private Role(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
    @Override
    public String getAuthority() {
        return name();
    }
}
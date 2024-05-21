package ru.project.BoardGames.Models;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

public enum Category {
    ADVENTURE("Приключенческие"),
    ECONOMIC("Экономические"),
    FAMILY("Семейные"),
    CARD("Карточные"),
    CHILD("Детские");


    private final String displayValue;

    private Category(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}

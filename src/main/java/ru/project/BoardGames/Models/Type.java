package ru.project.BoardGames.Models;

public enum Type {
        GAME("Игра"),
        ADDITION("Дополнение");

        private final String displayValue;

        private Type(String displayValue) {
                this.displayValue = displayValue;
        }

        public String getDisplayValue() {
                return displayValue;
        }
}

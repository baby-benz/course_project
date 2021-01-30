package ru.cafeteriaitmo.server.domain.enums;

public enum Type {
    BREAKFAST,
    STARTER,
    SECOND,
    DRINKING;

    Type() {
    }

    @Override
    public String toString() {
        switch(this) {
            case BREAKFAST: return "Завтрак";
            case STARTER: return "Первое";
            case SECOND: return "Второе";
            case DRINKING: return "Напиток";
            default: return "Еда";
        }
    }
}
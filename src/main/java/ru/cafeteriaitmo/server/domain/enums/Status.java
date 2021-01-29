package ru.cafeteriaitmo.server.domain.enums;

public enum Status {
    CREATED,
    PREPARING,
    PAYMENT,
    READY,
    CANCELLED;

    Status() {
    }

    @Override
    public String toString() {
        switch(this) {
            case CREATED: return "создан";
            case PREPARING: return "готовится";
            case PAYMENT: return "оплата";
            case READY: return "готов";
            case CANCELLED: return "отменен";
            default: return "уточните";
        }
    }
}

package ru.practicum.shareit.booking.model;

import java.util.Arrays;

public enum StatusBooking {
    WAITING("WAITING"), // новое бронирование, ожидает одобрения
    APPROVED("APPROVED"),   // бронирование подтверждено владельцем
    REJECTED("REJECTED"),   // бронирование отклонено владельцем
    CANCELED("CANCELED");    // бронирование отменено создателем

    private final String value;

    private StatusBooking(String value) {
        this.value = value;
    }

    public static StatusBooking fromString(final String s) throws IllegalArgumentException {
        return Arrays.stream(StatusBooking.values())
                .filter(v -> v.value.equals(s))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("Не определен статус бронирования : %s", s)));
    }

    public boolean isWaiting() {
        return this == StatusBooking.WAITING;
    }

    public boolean isApproved() {
        return this == StatusBooking.APPROVED;
    }

    public boolean isRejected() {
        return this == StatusBooking.REJECTED;
    }

    public boolean isCanceled() {
        return this == StatusBooking.CANCELED;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        switch (this) {
            case WAITING -> {
                return "Новое бронирование, ожидает одобрения владельца.";
            }
            case APPROVED -> {
                return "Бронирование подтверждено владельцем.";
            }
            case REJECTED -> {
                return "Бронирование отклонено владельцем.";
            }
            case CANCELED -> {
                return "Бронирование отменено создателем.";
            }
            default -> {
                return super.toString();
            }
        }
    }
}

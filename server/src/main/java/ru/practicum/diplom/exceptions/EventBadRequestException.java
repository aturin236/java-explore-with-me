package ru.practicum.diplom.exceptions;

public class EventBadRequestException extends RuntimeException {
    public EventBadRequestException(String message) {
        super(message);
    }
}

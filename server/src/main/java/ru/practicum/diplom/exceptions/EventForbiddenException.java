package ru.practicum.diplom.exceptions;

public class EventForbiddenException extends RuntimeException {
    public EventForbiddenException(String message) {
        super(message);
    }
}

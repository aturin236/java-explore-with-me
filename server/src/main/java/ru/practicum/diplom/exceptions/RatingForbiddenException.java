package ru.practicum.diplom.exceptions;

public class RatingForbiddenException extends RuntimeException {
    public RatingForbiddenException(String message) {
        super(message);
    }
}

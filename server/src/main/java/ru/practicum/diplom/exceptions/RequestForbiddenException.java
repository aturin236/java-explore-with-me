package ru.practicum.diplom.exceptions;

public class RequestForbiddenException extends RuntimeException {
    public RequestForbiddenException(String message) {
        super(message);
    }
}

package ru.practicum.diplom.exceptions;

public class CompilationBadRequestException extends RuntimeException {
    public CompilationBadRequestException(String message) {
        super(message);
    }
}

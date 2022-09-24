package ru.practicum.diplom.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler({UserNotFoundException.class,
            CategoryNotFoundException.class,
            EventNotFoundException.class,
            CompilationNotFoundException.class,
            RequestNotFoundException.class})
    public ResponseEntity<ApiError> handle404(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiError.builder()
                        .status(ApiErrorStatus.NOT_FOUND_404)
                        .reason("object not found")
                        .message(e.getLocalizedMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler({EventForbiddenException.class,
            RequestForbiddenException.class,
            RatingForbiddenException.class})
    public ResponseEntity<ApiError> handle403(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiError.builder()
                        .status(ApiErrorStatus.FORBIDDEN_403)
                        .reason("operation conditions violated")
                        .message(e.getLocalizedMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            MissingServletRequestParameterException.class,
            EventBadRequestException.class,
            CompilationBadRequestException.class})
    public ResponseEntity<ApiError> handle400(Exception e) {
        String error = "";
        if (e instanceof MethodArgumentNotValidException) {
            error = "error in object " + ((MethodArgumentNotValidException) e).getObjectName();
        } else if (e instanceof ConstraintViolationException) {
            error = ((ConstraintViolationException) e).getConstraintViolations().toString();
        } else if (e instanceof MissingServletRequestParameterException) {
            error = ((MissingServletRequestParameterException) e).getParameterName();
        }
        List<String> errors = List.of(error);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiError.builder()
                        .status(ApiErrorStatus.BAD_REQUEST_400)
                        .reason("invalid request parameters")
                        .message(e.getLocalizedMessage())
                        .errors(errors)
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler({RuntimeException.class,
            EventStatDtoInternalException.class})
    public ResponseEntity<ApiError> handle500(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiError.builder()
                        .status(ApiErrorStatus.INTERNAL_SERVER_ERROR_500)
                        .reason("Error occurred")
                        .message(e.getLocalizedMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}

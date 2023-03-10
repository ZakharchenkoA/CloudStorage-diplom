package ru.netology.cloudstorage.advice;

import ru.netology.cloudstorage.exception.FileNameNotUniqueException;
import ru.netology.cloudstorage.exception.FileNotFoundException;
import ru.netology.cloudstorage.exception.NewFileNameNotUniqueException;
import ru.netology.cloudstorage.exception.NewFileNameUnknownException;
import ru.netology.cloudstorage.dto.response.ErrorMessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static ru.netology.cloudstorage.generator.GeneratorId.getGeneratorId;
import static ru.netology.cloudstorage.utils.ErrorMessages.*;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileNameNotUniqueException.class)
    public ErrorMessageResponse handlerFileNameNotUniqueException(FileNameNotUniqueException ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. {}", errorId, ex.getMessage());
        return new ErrorMessageResponse(FILE_NAME_NOT_UNIQUE_MESSAGE, errorId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FileNotFoundException.class)
    public ErrorMessageResponse handlerFileNotFoundException(FileNotFoundException ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. {}", errorId, ex.getMessage());
        return new ErrorMessageResponse(FILE_NOT_FOUND_MESSAGE, errorId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NewFileNameUnknownException.class)
    public ErrorMessageResponse handlerNewFileNameUnknownException(NewFileNameUnknownException ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. {}", errorId, ex.getMessage());
        return new ErrorMessageResponse(NEW_FILE_NAME_UNKNOWN_MESSAGE, errorId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NewFileNameNotUniqueException.class)
    public ErrorMessageResponse handlerNewFileNameNotUniqueException(NewFileNameNotUniqueException ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. {}", errorId, ex.getMessage());
        return new ErrorMessageResponse(NEW_FILE_NAME_NOT_UNIQUE_MESSAGE, errorId);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorMessageResponse handlerAuthenticationException(AuthenticationException ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. AuthService. Authentication. {}.", errorId, ex.getMessage());
        return new ErrorMessageResponse(BAD_CREDENTIALS_MESSAGE, errorId);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({AuthenticationServiceException.class, Exception.class})
    public ErrorMessageResponse handlerException(Exception ex) {
        final var errorId = getGeneratorId().getId();
        log.error("ErrorId: [{}]. {}", errorId, ex.getMessage());
        return new ErrorMessageResponse(SERVER_ERROR_MESSAGE, errorId);
    }
}
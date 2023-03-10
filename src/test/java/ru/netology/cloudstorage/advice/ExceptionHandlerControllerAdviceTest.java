package ru.netology.cloudstorage.advice;

import ru.netology.cloudstorage.exception.FileNameNotUniqueException;
import ru.netology.cloudstorage.exception.FileNotFoundException;
import ru.netology.cloudstorage.exception.NewFileNameNotUniqueException;
import ru.netology.cloudstorage.exception.NewFileNameUnknownException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.stream.Stream;

import static ru.netology.cloudstorage.testdata.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerControllerAdviceTest {

    private static Stream<Arguments> sourceForHandlerException() {
        return Stream.of(Arguments.of(new AuthenticationServiceException(ERROR_MESSAGE)),
                Arguments.of(new Exception(ERROR_MESSAGE)));
    }

    @InjectMocks
    ExceptionHandlerControllerAdvice exceptionHandlerControllerAdvice;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testHandlerFileNameNotUniqueException() {
        final var exception = new FileNameNotUniqueException(ERROR_MESSAGE);

        final var actualResponse = exceptionHandlerControllerAdvice.handlerFileNameNotUniqueException(exception);

        assertEquals(MESSAGE_FILE_NAME_NOT_UNIQUE, actualResponse.getMessage());
    }

    @Test
    public void testHandlerFileNotFoundException() {
        final var exception = new FileNotFoundException(ERROR_MESSAGE);

        final var actualResponse = exceptionHandlerControllerAdvice.handlerFileNotFoundException(exception);

        assertEquals(MESSAGE_FILE_NOT_FOUND, actualResponse.getMessage());
    }

    @Test
    public void testHandlerNewFileNameUnknownException() {
        final var exception = new NewFileNameUnknownException(ERROR_MESSAGE);

        final var actualResponse = exceptionHandlerControllerAdvice.handlerNewFileNameUnknownException(exception);

        assertEquals(MESSAGE_NEW_FILE_NAME_UNKNOWN, actualResponse.getMessage());
    }

    @Test
    public void testHandlerNewFileNameNotUniqueException() {
        final var exception = new NewFileNameNotUniqueException(ERROR_MESSAGE);

        final var actualResponse = exceptionHandlerControllerAdvice.handlerNewFileNameNotUniqueException(exception);

        assertEquals(MESSAGE_NEW_FILE_NAME_NOT_UNIQUE, actualResponse.getMessage());
    }

    @Test
    public void testHandlerAuthenticationException() {
        final var exception = new BadCredentialsException(ERROR_MESSAGE);

        final var actualResponse = exceptionHandlerControllerAdvice.handlerAuthenticationException(exception);

        assertEquals(MESSAGE_BAD_CREDENTIALS, actualResponse.getMessage());
    }

    @ParameterizedTest
    @MethodSource("sourceForHandlerException")
    public void testHandlerException(Exception exception) {
        final var actualResponse = exceptionHandlerControllerAdvice.handlerException(exception);

        assertEquals(MESSAGE_SERVER_ERROR, actualResponse.getMessage());
    }
}
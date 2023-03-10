package ru.netology.cloudstorage.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static ru.netology.cloudstorage.testdata.TestData.CONTENT_TYPE;
import static ru.netology.cloudstorage.testdata.TestData.ERROR_MESSAGE;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TokenAuthenticationEntryPointTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    AuthenticationException authException;
    @Mock
    PrintWriter printWriter;
    @InjectMocks
    TokenAuthenticationEntryPoint tokenAuthenticationEntryPoint;

    @BeforeEach
    public void setUp() {
        out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        out.println("The test is completed: " + this);
    }

    @Test
    public void testCommenceIfHttpStatusInternalServerErrorSuccess() throws IOException {
        when(response.getStatus()).thenReturn(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        tokenAuthenticationEntryPoint.commence(request, response, authException);

        assertEquals(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, response.getStatus());
        verify(response, never()).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response, never()).setContentType(CONTENT_TYPE);
        verify(response, never()).getWriter();
    }

    @Test
    public void testCommenceIfHttpStatusUnauthorizedSuccess() throws IOException {
        when(response.getStatus()).thenReturn(HttpServletResponse.SC_UNAUTHORIZED);
        when(authException.getMessage()).thenReturn(ERROR_MESSAGE);
        when(response.getWriter()).thenReturn(printWriter);

        tokenAuthenticationEntryPoint.commence(request, response, authException);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(response, times(1)).setContentType(CONTENT_TYPE);
    }
}
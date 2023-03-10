package ru.netology.cloudstorage.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.netology.cloudstorage.utils.ApplicationData;
import ru.netology.cloudstorage.utils.ErrorMessages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.netology.cloudstorage.generator.GeneratorId.getGeneratorId;

@Component
@Slf4j
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        if (response.getStatus() != HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
            final var errorId = getGeneratorId().getId();
            log.error("ErrorId: [{}]. TokenFilter. Token authentication. {}.", errorId, authException.getMessage());
            formResponse(response, errorId);
        }
    }

    protected void formResponse(HttpServletResponse response, Integer errorId) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(ApplicationData.CUSTOM_CONTENT_TYPE);
        response.getWriter().write(String.format(ApplicationData.FORMAT_MESSAGE, ErrorMessages.UNAUTHORIZED_ERROR_MESSAGE, errorId));
    }
}
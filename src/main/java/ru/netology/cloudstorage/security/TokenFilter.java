package ru.netology.cloudstorage.security;

import ru.netology.cloudstorage.service.TokenService;
import ru.netology.cloudstorage.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.cloudstorage.utils.ApplicationData;
import ru.netology.cloudstorage.utils.ErrorMessages;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.netology.cloudstorage.generator.GeneratorId.getGeneratorId;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authToken = getTokenFromRequest(request);
        if (authToken != null && tokenService.isTokenInStorage(authToken)) {
            if (tokenService.checkToken(authToken)) {
                final var userId = tokenService.getUserIdFromToken(authToken);
                final var user = userService.getUser(userId);
                try {
                    final var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            user.getAuthorities()
                    );
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                } catch (Exception ex) {
                    final var errorId = getGeneratorId().getId();
                    log.error("ErrorId: [{}]. TokenFilter. {}", errorId, ex.getMessage());
                    formResponse(response, errorId);
                }
            } else {
                tokenService.removeTokenFromStorage(authToken);
                log.info("The access token has been removed from storage.");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final var valueHeaderAuthToken = request.getHeader(ApplicationData.HEADER_AUTH_TOKEN);
        if (StringUtils.hasText(valueHeaderAuthToken) && valueHeaderAuthToken.startsWith(ApplicationData.BEARER)) {
            return valueHeaderAuthToken.substring(ApplicationData.BEGIN_INDEX);
        }
        return null;
    }

    protected void formResponse(HttpServletResponse response, Integer errorId) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.setContentType(ApplicationData.CUSTOM_CONTENT_TYPE);
        response.getWriter().write(String.format(ApplicationData.FORMAT_MESSAGE, ErrorMessages.SERVER_ERROR_MESSAGE, errorId));
    }
}
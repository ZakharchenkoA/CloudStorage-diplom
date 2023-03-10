package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static ru.netology.cloudstorage.testdata.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Nested
    public class methodLoadUserByUsernameTest {

        @Test
        public void testLoadUserByUsernameSuccess() {
            when(userRepository.findByLogin(LOGIN)).thenReturn(Optional.of(USER_ENTITY));

            UserDetails actualUser = userService.loadUserByUsername(LOGIN);

            assertEquals(USER, actualUser);
        }

        @Test
        public void testLoadUserByUsernameThrowsUsernameNotFoundException() {
            when(userRepository.findByLogin(LOGIN)).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(LOGIN));
        }
    }

    @Test
    public void testGetUserSuccess() {
        final var actualUser = userService.getUser(ID);

        assertEquals(USER_ONLY_ID, actualUser);
    }

    @Test
    public void testGetUserEntityFromUserSuccess() {
        final var actualUser = userService.getUserEntityFromUser(USER_ONLY_ID);

        assertEquals(USER_ENTITY_ONLY_ID, actualUser);
    }

    @Test
    public void testGetUserAuthSuccess() {
        final var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                USER,
                PASSWORD,
                USER.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        final var actualUser = userService.getUserAuth();

        assertEquals(USER, actualUser);
    }
}
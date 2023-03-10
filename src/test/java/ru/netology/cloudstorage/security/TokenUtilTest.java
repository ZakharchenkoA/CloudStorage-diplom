package ru.netology.cloudstorage.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static ru.netology.cloudstorage.testdata.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TokenUtilTest {

    @InjectMocks
    private TokenUtil tokenUtil;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
        tokenUtil.setLifetime(LIFETIME);
        tokenUtil.setSecretKey(SECRET_KEY);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testGenerateTokenSuccess() {
        final var actualResult = tokenUtil.generateToken(USER_ONLY_ID);

        assertInstanceOf(String.class, actualResult);
    }

    @Nested
    public class methodCheckTokenTest {

        @Test
        public void testCheckTokenSuccess() {
            final var token = tokenUtil.generateToken(USER_ONLY_ID);

            final var actualResult = tokenUtil.checkToken(token);

            assertTrue(actualResult);
        }

        @Test
        public void testCheckTokenInvalid() {
            final var actualResult = tokenUtil.checkToken(TOKEN_ONE);

            assertFalse(actualResult);
        }
    }

    @Test
    public void testGetUserIdFromTokenSuccess() {
        final var token = tokenUtil.generateToken(USER_ONLY_ID);

        final var actualResult = tokenUtil.getUserIdFromToken(token);

        assertEquals(ID, actualResult);
    }
}
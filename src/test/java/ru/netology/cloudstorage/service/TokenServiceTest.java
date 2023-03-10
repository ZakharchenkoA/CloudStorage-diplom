package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.repository.TokenRepository;
import ru.netology.cloudstorage.security.TokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ru.netology.cloudstorage.testdata.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;
    @Mock
    private TokenUtil tokenUtil;
    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testPutTokenAndUserLoginInStorageSuccess() {
        tokenService.addTokenInStorage(TOKEN_ONE);

        verify(tokenRepository, times(1)).addToken(TOKEN_ONE);
    }

    @Test
    public void testRemoveTokenFromStorageSuccess() {
        tokenService.removeTokenFromStorage(TOKEN_ONE);

        verify(tokenRepository, times(1)).removeToken(TOKEN_ONE);
    }

    @Test
    public void testIsTokenInStorageSuccess() {
        when(tokenRepository.isToken(TOKEN_ONE)).thenReturn(true);

        final var actualResult = tokenService.isTokenInStorage(TOKEN_ONE);

        assertTrue(actualResult);
    }

    @Test
    public void testGetNumberOfRecordsInStorageSuccess() {
        when(tokenRepository.getSizeTokens()).thenReturn(NUMBER_OF_TOKENS);

        final var actualResult = tokenService.getNumberOfTokensInStorage();

        assertEquals(NUMBER_OF_TOKENS, actualResult);
    }

    @Test
    public void testGetAllTokensFromStorageSuccess() {
        when(tokenRepository.getAllTokens()).thenReturn(List.of(TOKEN_ONE, TOKEN_TWO));

        final var actualResult = tokenService.getAllTokensFromStorage();

        assertEquals(List.of(TOKEN_ONE, TOKEN_TWO), actualResult);
    }

    @Test
    public void testGenerateTokenSuccess() {
        when(tokenUtil.generateToken(USER)).thenReturn(TOKEN_ONE);

        final var actualResult = tokenService.generateToken(USER);

        assertEquals(TOKEN_ONE, actualResult);
    }

    @Test
    public void testCheckTokenSuccess() {
        when(tokenUtil.checkToken(TOKEN_ONE)).thenReturn(true);

        final var actualResult = tokenService.checkToken(TOKEN_ONE);

        assertTrue(actualResult);
    }

    @Test
    public void testClearTheStorageOfInvalidTokensSuccess() {
        when(tokenRepository.getSizeTokens()).thenReturn(NUMBER_OF_TOKENS);
        when(tokenRepository.getAllTokens()).thenReturn(List.of(TOKEN_ONE, TOKEN_TWO));
        when(tokenUtil.checkToken(TOKEN_ONE)).thenReturn(true);
        when(tokenUtil.checkToken(TOKEN_TWO)).thenReturn(false);

        tokenService.clearTheStorageOfInvalidTokens();

        verify(tokenRepository, times(1)).removeToken(TOKEN_TWO);
    }
}
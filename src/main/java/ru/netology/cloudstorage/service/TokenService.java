package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.model.User;
import ru.netology.cloudstorage.repository.TokenRepository;
import ru.netology.cloudstorage.security.TokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;
    private final TokenUtil tokenUtil;

    public void addTokenInStorage(String token) {
        tokenRepository.addToken(token);
    }

    public void removeTokenFromStorage(String token) {
        tokenRepository.removeToken(token);
    }

    public boolean isTokenInStorage(String token) {
        return tokenRepository.isToken(token);
    }

    public int getNumberOfTokensInStorage() {
        return tokenRepository.getSizeTokens();
    }

    public List<String> getAllTokensFromStorage() {
        return tokenRepository.getAllTokens();
    }

    public String generateToken(User user) {
        return tokenUtil.generateToken(user);
    }

    public boolean checkToken(String token) {
        return tokenUtil.checkToken(token);
    }

    public Long getUserIdFromToken(String token) {
        return tokenUtil.getUserIdFromToken(token);
    }

    @Scheduled(fixedDelayString = "${jwt.token.check-interval}")
    public void clearTheStorageOfInvalidTokens() {
        log.info("The operation of deleting invalid access tokens from the storage has been started." +
                " Total tokens in storage: [{}].", getNumberOfTokensInStorage());
        final var tokenAll = getAllTokensFromStorage();
        int numberOfDeletedTokens = 0;
        if (!tokenAll.isEmpty()) {
            for (final var token : tokenAll) {
                if (!checkToken(token)) {
                    removeTokenFromStorage(token);
                    numberOfDeletedTokens++;
                }
            }
        }
        log.info("The operation of deleting invalid access tokens from the storage has been completed." +
                " Deleted: [{}].", numberOfDeletedTokens);
    }
}
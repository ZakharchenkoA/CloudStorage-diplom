package ru.netology.cloudstorage.repository;

import java.util.List;

public interface TokenRepository {

    void addToken(String token);

    void removeToken(String token);

    boolean isToken(String token);

    int getSizeTokens();

    List<String> getAllTokens();
}
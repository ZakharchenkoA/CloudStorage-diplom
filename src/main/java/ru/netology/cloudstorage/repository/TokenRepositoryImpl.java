package ru.netology.cloudstorage.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Repository
public class TokenRepositoryImpl implements TokenRepository {

    private final static Set<String> tokens = new ConcurrentSkipListSet<>();

    @Override
    public void addToken(String token) {
        tokens.add(token);
    }

    @Override
    public void removeToken(String token) {
        tokens.remove(token);
    }

    @Override
    public boolean isToken(String token) {
        return tokens.contains(token);
    }

    @Override
    public int getSizeTokens() {
        return tokens.size();
    }

    @Override
    public List<String> getAllTokens() {
        return tokens.stream().toList();
    }
}
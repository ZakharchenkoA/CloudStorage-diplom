package ru.netology.cloudstorage;

import ru.netology.cloudstorage.entity.UserEntity;
import ru.netology.cloudstorage.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        List<UserEntity> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            userRepository.save(UserEntity.builder()
                    .login("user1@mail.com")
                    .password(passwordEncoder.encode("pass1"))
                    .build());
            userRepository.save(UserEntity.builder()
                    .login("user2@mail.com")
                    .password(passwordEncoder.encode("pass2"))
                    .build());
        }
    }
}
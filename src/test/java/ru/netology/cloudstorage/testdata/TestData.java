package ru.netology.cloudstorage.testdata;

import ru.netology.cloudstorage.model.User;
import ru.netology.cloudstorage.dto.request.AuthRequest;
import ru.netology.cloudstorage.dto.response.AuthResponse;
import ru.netology.cloudstorage.dto.request.FileNameRequest;
import ru.netology.cloudstorage.dto.response.FileResponse;
import ru.netology.cloudstorage.entity.FileEntity;
import ru.netology.cloudstorage.entity.UserEntity;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class TestData {

    public static final String AUTH_TOKEN_EMPTY = "";
    public static final String AUTH_TOKEN_INVALID = "authTokenInvalid";
    public static final String BEARER = "Bearer ";
    public static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    public static final String ERROR_MESSAGE = "Error message";
    public static final String FILE_BODY = "Test content.";
    public static final String FILE_NAME = "test.txt";
    public static final String FILE_NAME_TWO = "tests.txt";
    public static final Long FILE_SIZE = 13L;
    public static final String FILE_TYPE = "text/plain";
    public static final String HEADER_AUTH_TOKEN = "auth-token";
    public static final Long ID = 1L;
    public static final String INVALID_VALUE = "invalid value";
    public static final Long LIFETIME = 60L;
    public static final Integer LIMIT = 3;
    public static final String LOGIN = "test@mail.com";
    public static final String MESSAGE_BAD_CREDENTIALS = "Ошибка ввода данных. Неверные учётные данные.";
    public static final String MESSAGE_FILE_NAME_NOT_UNIQUE =
            "Ошибка ввода данных. Файл с таким именем уже есть в облаке.";
    public static final String MESSAGE_FILE_NOT_FOUND = "Ошибка ввода данных. Файл с таким именем не найден.";
    public static final String MESSAGE_NEW_FILE_NAME_UNKNOWN = "Ошибка ввода данных. Отсутствует новое имя файла.";
    public static final String MESSAGE_NEW_FILE_NAME_NOT_UNIQUE =
            "Ошибка ввода данных. Новое имя файла совпадает с именем файла в облаке.";
    public static final String MESSAGE_SERVER_ERROR =
            "Ошибка сервера. Попробуйте повторить операцию через какое-то время.";
    public static final String NEW_FILE_NAME = "new.txt";
    public static final String NEW_FILE_NAME_EMPTY = " ";
    public static final int NUMBER_OF_TOKENS = 2;
    public static final Integer NUMBER_ONE = 1;
    public static final Integer NUMBER_TWO = 2;
    public static final Integer NUMBER_ZERO = 0;
    public static final String PASSWORD = "test";
    public static final String PASSWORD_ENCODED = "$2a$10$G.rPhcA1V8KRkVZ7yzVjwO0sGL1JYXR0fMp8gTLN3LkxXUKVD3Uf6";
    public static final String QUERY_PARAM_FILENAME = "filename";
    public static final String QUERY_PARAM_LIMIT = "limit";
    public static final String SECRET_KEY = "testSecretKey";
    public static final String TOKEN_ONE = "tokenOne";
    public static final String TOKEN_TWO = "tokenTwo";
    public static final String URL_FILE = "/cloud/file";
    public static final String URL_LIST = "/cloud/list";
    public static final String URL_LOGIN = "/cloud/login";
    public static final String URL_LOGOUT = "/cloud/logout";
    public static final String VALUE_HEADER_AUTH_TOKEN = "Bearer tokenOne";
    public static final String VALUE_HEADER_INVALID_ONE = "";
    public static final String VALUE_HEADER_INVALID_TWO = "Bearer  tokenOne";

    public static final AuthRequest AUTH_REQUEST = new AuthRequest(LOGIN, PASSWORD);
    public static final AuthResponse AUTH_RESPONSE = new AuthResponse(TOKEN_ONE);
    public static final FileEntity FILE_ENTITY = FileEntity.builder()
            .id(ID)
            .fileName(FILE_NAME)
            .fileSize(FILE_SIZE)
            .fileType(FILE_TYPE)
            .build();
    public static final FileNameRequest FILE_NAME_REQUEST = new FileNameRequest(NEW_FILE_NAME);
    public static final FileResponse FILE_RESPONSE = new FileResponse(FILE_NAME, FILE_SIZE);
    public static final MockMultipartFile MULTIPART_FILE =
            new MockMultipartFile("file", FILE_NAME, MediaType.TEXT_PLAIN_VALUE, FILE_BODY.getBytes());
    public static final User USER = User.builder()
            .id(ID)
            .login(LOGIN)
            .password(PASSWORD)
            .build();
    public static final User USER_ONLY_ID = User.builder()
            .id(ID)
            .build();
    public static final UserEntity USER_ENTITY = UserEntity.builder()
            .id(ID)
            .login(LOGIN)
            .password(PASSWORD)
            .build();
    public static final UserEntity USER_ENTITY_ONLY_ID = UserEntity.builder()
            .id(ID)
            .build();
}
package ru.netology.cloudstorage.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.netology.cloudstorage.model.User;
import ru.netology.cloudstorage.dto.request.FileNameRequest;
import ru.netology.cloudstorage.dto.response.FileResponse;
import ru.netology.cloudstorage.entity.FileEntity;
import ru.netology.cloudstorage.entity.UserEntity;
import ru.netology.cloudstorage.repository.FileRepository;
import ru.netology.cloudstorage.repository.UserRepository;
import ru.netology.cloudstorage.service.TokenService;
import ru.netology.cloudstorage.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.netology.cloudstorage.testdata.TestData;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class FileIntegrationTest extends AbstractIntegrationTest {

    private static Stream<Arguments> sourceForErrorIsBadRequest() throws JsonProcessingException {
        final var contentNewFileNameEmpty = createContent(TestData.NEW_FILE_NAME_EMPTY);
        final var contentNewFileNameNew = createContent(TestData.NEW_FILE_NAME);
        final var contentNewFileNameDuplicate = createContent(TestData.FILE_NAME);
        return Stream.of(
                Arguments.of(
                        MockMvcRequestBuilders.multipart(TestData.URL_FILE)
                                .file(TestData.MULTIPART_FILE)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME)
                ),
                Arguments.of(
                        MockMvcRequestBuilders.delete(TestData.URL_FILE)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME_TWO)
                ),
                Arguments.of(
                        MockMvcRequestBuilders.get(TestData.URL_FILE)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME_TWO)
                ),
                Arguments.of(
                        MockMvcRequestBuilders.put(TestData.URL_FILE)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME)
                                .content(contentNewFileNameEmpty)
                                .contentType(MediaType.APPLICATION_JSON)
                ),
                Arguments.of(
                        MockMvcRequestBuilders.put(TestData.URL_FILE)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME_TWO)
                                .content(contentNewFileNameNew)
                                .contentType(MediaType.APPLICATION_JSON)
                ),
                Arguments.of(
                        MockMvcRequestBuilders.put(TestData.URL_FILE)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME)
                                .content(contentNewFileNameDuplicate)
                                .contentType(MediaType.APPLICATION_JSON)
                )
        );
    }

    private static String createContent(String fileName) throws JsonProcessingException {
        final var fileNameRequest = new FileNameRequest(fileName);
        return new ObjectMapper().writeValueAsString(fileNameRequest);
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileRepository fileRepository;

    private String authToken;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
        userRepository.deleteAll();
        fileRepository.deleteAll();
        addTestUser();
        authToken = getAndSaveToken();
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testUploadFileSuccess() throws Exception {
        mockMvc
                .perform(
                        MockMvcRequestBuilders.multipart(TestData.URL_FILE)
                                .file(TestData.MULTIPART_FILE)
                                .header(TestData.HEADER_AUTH_TOKEN, TestData.BEARER + authToken)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteFileSuccess() throws Exception {
        final var testUser = getTestUser();
        addTestFile(testUser, TestData.FILE_NAME);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(TestData.URL_FILE)
                                .header(TestData.HEADER_AUTH_TOKEN, TestData.BEARER + authToken)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testDownloadFileSuccess() throws Exception {
        final var testUser = getTestUser();
        addTestFile(testUser, TestData.FILE_NAME);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.get(TestData.URL_FILE)
                                .header(TestData.HEADER_AUTH_TOKEN, TestData.BEARER + authToken)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testEditFileSuccess() throws Exception {
        final var testUser = getTestUser();
        addTestFile(testUser, TestData.FILE_NAME);
        final var fileNameRequest = new FileNameRequest(TestData.NEW_FILE_NAME);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.put(TestData.URL_FILE)
                                .header(TestData.HEADER_AUTH_TOKEN, TestData.BEARER + authToken)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME)
                                .content(objectMapper.writeValueAsString(fileNameRequest))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllFilesSuccess() throws Exception {
        final var testUser = getTestUser();
        addTestFile(testUser, TestData.FILE_NAME);
        addTestFile(testUser, TestData.FILE_NAME_TWO);
        final var list = fileRepository.findAllByUserId(testUser.getId()).stream()
                .map(file -> new FileResponse(file.getFileName(), file.getFileSize()))
                .collect(Collectors.toList());

        mockMvc
                .perform(
                        MockMvcRequestBuilders.get(TestData.URL_LIST)
                                .header(TestData.HEADER_AUTH_TOKEN, TestData.BEARER + authToken)
                                .queryParam(TestData.QUERY_PARAM_LIMIT, String.valueOf(TestData.LIMIT))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(list)));
    }

    @ParameterizedTest
    @MethodSource("sourceForErrorIsBadRequest")
    public void testErrorIsBadRequest(MockHttpServletRequestBuilder requestBuilder) throws Exception {
        final var testUser = getTestUser();
        addTestFile(testUser, TestData.FILE_NAME);
        requestBuilder.header(TestData.HEADER_AUTH_TOKEN, TestData.BEARER + authToken);

        mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @ValueSource(strings = {TestData.AUTH_TOKEN_EMPTY, TestData.AUTH_TOKEN_INVALID})
    public void testErrorIsUnauthorized(String token) throws Exception {
        final var testUser = getTestUser();
        tokenService.addTokenInStorage(TestData.AUTH_TOKEN_INVALID);
        addTestFile(testUser, TestData.FILE_NAME);

        mockMvc
                .perform(
                        MockMvcRequestBuilders.delete(TestData.URL_FILE)
                                .header(TestData.HEADER_AUTH_TOKEN, TestData.BEARER + token)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testErrorIsInternalServerError() throws Exception {
        userRepository.deleteAll();

        mockMvc
                .perform(
                        MockMvcRequestBuilders.multipart(TestData.URL_FILE)
                                .file(TestData.MULTIPART_FILE)
                                .header(TestData.HEADER_AUTH_TOKEN, TestData.BEARER + authToken)
                                .queryParam(TestData.QUERY_PARAM_FILENAME, TestData.FILE_NAME)
                )
                .andExpect(status().isInternalServerError());
    }

    private String getAndSaveToken() {
        final var testUser = getTestUser();
        final var authToken = tokenService.generateToken(testUser);
        tokenService.addTokenInStorage(authToken);
        return authToken;
    }

    private void addTestUser() {
        final var userEntity = UserEntity.builder()
                .login(TestData.LOGIN)
                .password(TestData.PASSWORD_ENCODED)
                .build();
        userRepository.save(userEntity);
    }

    private User getTestUser() {
        return userRepository.findByLogin(TestData.LOGIN)
                .map(user -> new User(
                        user.getId(),
                        user.getLogin(),
                        user.getPassword()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("The user was not found in the database."));
    }

    private void addTestFile(User testUser, String fileName) {
        final var fileEntity = FileEntity.builder()
                .fileName(fileName)
                .fileSize(TestData.FILE_SIZE)
                .fileType(MediaType.TEXT_PLAIN_VALUE)
                .fileDateUpdate(LocalDateTime.now())
                .fileByte(TestData.FILE_BODY.getBytes())
                .user(userService.getUserEntityFromUser(testUser))
                .build();
        fileRepository.save(fileEntity);
    }
}
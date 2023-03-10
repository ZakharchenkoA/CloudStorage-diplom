package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.exception.FileNameNotUniqueException;
import ru.netology.cloudstorage.exception.FileNotFoundException;
import ru.netology.cloudstorage.exception.NewFileNameNotUniqueException;
import ru.netology.cloudstorage.exception.NewFileNameUnknownException;
import ru.netology.cloudstorage.dto.response.FileResponse;
import ru.netology.cloudstorage.entity.FileEntity;
import ru.netology.cloudstorage.repository.FileRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static ru.netology.cloudstorage.testdata.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {

    @Mock
    private FileRepository fileRepository;
    @Mock
    private UserService userService;
    @InjectMocks
    private FileService fileService;

    @BeforeEach
    public void setUp() {
        when(userService.getUserEntityFromUser(userService.getUserAuth())).thenReturn(USER_ENTITY_ONLY_ID);
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Nested
    public class methodUploadFileTest {

        @Test
        public void testUploadFileSuccess() throws IOException {
            when(fileRepository.findByUserIdAndFileName(ID, FILE_NAME)).thenReturn(Optional.empty());

            fileService.uploadFile(FILE_NAME, MULTIPART_FILE);

            verify(fileRepository, times(1)).save(any(FileEntity.class));
        }

        @Test
        public void testUploadFileThrowsFileNameNotUniqueException() {
            when(fileRepository.findByUserIdAndFileName(ID, FILE_NAME)).thenReturn(Optional.of(FILE_ENTITY));

            assertThrows(FileNameNotUniqueException.class, () -> fileService.uploadFile(FILE_NAME, MULTIPART_FILE));
        }
    }

    @Nested
    public class methodDeleteFileTest {

        @Test
        public void testDeleteFileSuccess() {
            when(fileRepository.findByUserIdAndFileName(ID, FILE_NAME)).thenReturn(Optional.of(FILE_ENTITY));

            fileService.deleteFile(FILE_NAME);

            verify(fileRepository, times(1)).deleteFileByUserIdAndFileName(ID, FILE_NAME);
        }

        @Test
        public void testDeleteFileThrowsFileNotFoundException() {
            when(fileRepository.findByUserIdAndFileName(ID, FILE_NAME)).thenReturn(Optional.empty());

            assertThrows(FileNotFoundException.class, () -> fileService.deleteFile(FILE_NAME));
        }
    }

    @Nested
    public class methodDownloadFileTest {

        @Test
        public void testDownloadFileSuccess() {
            when(fileRepository.findByUserIdAndFileName(ID, FILE_NAME)).thenReturn(Optional.of(FILE_ENTITY));

            final var actualFileEntity = fileService.downloadFile(FILE_NAME);

            assertEquals(FILE_ENTITY, actualFileEntity);
        }

        @Test
        public void testDownloadFileThrowsFileNotFoundException() {
            when(fileRepository.findByUserIdAndFileName(ID, FILE_NAME)).thenReturn(Optional.empty());

            assertThrows(FileNotFoundException.class, () -> fileService.downloadFile(FILE_NAME));
        }
    }

    @Nested
    public class methodEditFileNameTest {

        @Test
        public void testEditFileNameSuccess() {
            when(fileRepository.findByUserIdAndFileName(ID, FILE_NAME)).thenReturn(Optional.of(FILE_ENTITY));

            fileService.editFileName(FILE_NAME, NEW_FILE_NAME);

            verify(fileRepository, times(1))
                    .editFileNameByUserId(NEW_FILE_NAME, FILE_NAME, USER_ENTITY_ONLY_ID.getId());
        }

        @Test
        public void testEditFileNameThrowsNewFileNameUnknownException() {
            assertThrows(NewFileNameUnknownException.class, () ->
                    fileService.editFileName(FILE_NAME, NEW_FILE_NAME_EMPTY));
        }

        @Test
        public void testEditFileNameThrowsFileNotFoundException() {
            when(fileRepository.findByUserIdAndFileName(ID, FILE_NAME)).thenReturn(Optional.empty());

            assertThrows(FileNotFoundException.class, () -> fileService.editFileName(FILE_NAME, NEW_FILE_NAME));
        }

        @Test
        public void testEditFileNameThrowsNewFileNameNotUniqueException() {
            when(fileRepository.findByUserIdAndFileName(ID, FILE_NAME)).thenReturn(Optional.of(FILE_ENTITY));

            assertThrows(NewFileNameNotUniqueException.class, () -> fileService.editFileName(FILE_NAME, FILE_NAME));
        }
    }

    @Test
    public void testGetAllFilesSuccess() {
        final var expectedFileResponse = List.of(new FileResponse(FILE_NAME, FILE_SIZE));
        when(fileRepository.findAllByUserId(USER_ENTITY_ONLY_ID.getId())).thenReturn(List.of(FILE_ENTITY));

        final var actualFileResponse = fileService.getAllFiles(LIMIT);

        assertEquals(expectedFileResponse, actualFileResponse);
    }
}
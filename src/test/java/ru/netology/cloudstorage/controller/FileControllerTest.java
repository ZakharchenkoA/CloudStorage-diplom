package ru.netology.cloudstorage.controller;

import ru.netology.cloudstorage.service.FileService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import ru.netology.cloudstorage.testdata.TestData;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @Mock
    private FileService fileService;
    @InjectMocks
    private FileController fileController;

    @BeforeEach
    public void setUp() {
        System.out.println("The test is running: " + this);
    }

    @AfterEach
    public void tearDown() {
        System.out.println("The test is completed: " + this);
    }

    @Test
    public void testUploadFileHttpStatusOkSuccess() throws IOException {
        final var actualResult = fileController.uploadFile(TestData.FILE_NAME, TestData.MULTIPART_FILE);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(fileService, times(1)).uploadFile(TestData.FILE_NAME, TestData.MULTIPART_FILE);
    }

    @Test
    public void testDeleteFileHttpStatusOkSuccess() {
        final var actualResult = fileController.deleteFile(TestData.FILE_NAME);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(fileService, times(1)).deleteFile(TestData.FILE_NAME);
    }

    @Test
    public void testDownloadFileHttpStatusOkSuccess() {
        when(fileService.downloadFile(TestData.FILE_NAME)).thenReturn(TestData.FILE_ENTITY);

        final var actualResult = fileController.downloadFile(TestData.FILE_NAME);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(fileService, times(1)).downloadFile(TestData.FILE_NAME);
    }

    @Test
    public void testEditFileNameHttpStatusOkSuccess() {
        final var actualResult = fileController.editFileName(TestData.FILE_NAME, TestData.FILE_NAME_REQUEST);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(fileService, times(1)).editFileName(TestData.FILE_NAME, TestData.NEW_FILE_NAME);
    }

    @Test
    public void testGetAllFilesHttpStatusOkSuccess() {
        when(fileService.getAllFiles(TestData.LIMIT)).thenReturn(List.of(TestData.FILE_RESPONSE));

        final var actualResult = fileController.getAllFiles(TestData.LIMIT);

        assertEquals(HttpStatus.OK, actualResult.getStatusCode());
        verify(fileService, times(1)).getAllFiles(TestData.LIMIT);
    }
}
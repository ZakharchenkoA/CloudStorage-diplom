package ru.netology.cloudstorage.service;

import ru.netology.cloudstorage.exception.FileNameNotUniqueException;
import ru.netology.cloudstorage.exception.FileNotFoundException;
import ru.netology.cloudstorage.exception.NewFileNameNotUniqueException;
import ru.netology.cloudstorage.exception.NewFileNameUnknownException;
import ru.netology.cloudstorage.dto.response.FileResponse;
import ru.netology.cloudstorage.entity.FileEntity;
import ru.netology.cloudstorage.entity.UserEntity;
import ru.netology.cloudstorage.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final UserService userService;

    @Transactional
    public void uploadFile(String fileName, MultipartFile file) throws IOException, FileNameNotUniqueException {
        final var userId = getUserAuth().getId();
        if (isFile(userId, fileName)) {
            throw new FileNameNotUniqueException(String.format("FileService. Upload file. A file with that name already" +
                    " exists. UserId: [%d], fileName: [%s].", userId, fileName));
        }
        fileRepository.save(createFileEntity(fileName, file));
        log.info("The user with the ID [{}] uploaded the file [{}].", userId, fileName);
    }

    @Transactional
    public void deleteFile(String fileName) throws FileNotFoundException {
        final var userId = getUserAuth().getId();
        if (!isFile(userId, fileName)) {
            throw new FileNotFoundException(String.format("FileService. Delete file. File not found. UserId: [%d]," +
                    " fileName: [%s].", userId, fileName));
        }
        fileRepository.deleteFileByUserIdAndFileName(userId, fileName);
        log.info("The user with the ID [{}] deleted the file [{}].", userId, fileName);
    }

    public FileEntity downloadFile(String fileName) throws FileNotFoundException {
        final var userId = getUserAuth().getId();
        final var fileEntity = fileRepository.findByUserIdAndFileName(userId, fileName);
        if (fileEntity.isEmpty()) {
            throw new FileNotFoundException(String.format("FileService. Download file. File not found. UserId: [%d]," +
                    " fileName: [%s].", userId, fileName));
        }
        log.info("The user with the ID [{}] downloaded the file [{}].", userId, fileName);
        return fileEntity.get();
    }

    @Transactional
    public void editFileName(String fileName, String newFileName) throws NewFileNameUnknownException,
            FileNotFoundException, NewFileNameNotUniqueException {
        final var userId = getUserAuth().getId();
        if (newFileName.trim().isEmpty()) {
            throw new NewFileNameUnknownException(String.format("FileService. Edit file. There is no new file name." +
                    " UserId: [%d], newFileName [%s].", userId, newFileName));
        }
        if (!isFile(userId, fileName)) {
            throw new FileNotFoundException(String.format("FileService. Edit file. File not found. UserId: [%d]," +
                    " fileName [%s].", userId, fileName));
        }
        if (isFile(userId, newFileName)) {
            throw new NewFileNameNotUniqueException(String.format("FileService. Edit file. Is the new file name the same" +
                    " as the file name in the cloud. UserId: [%d], newFileName [%s].", userId, newFileName));
        }
        fileRepository.editFileNameByUserId(newFileName, fileName, userId);
        log.info("The user with the ID [{}] changed the file name [{}] to [{}].", userId, fileName, newFileName);
    }

    public List<FileResponse> getAllFiles(Integer limit) {
        final var userId = getUserAuth().getId();
        log.info("The user with the ID [{}] received a list of files in the cloud.", userId);
        return fileRepository.findAllByUserId(userId).stream()
                .map(file -> new FileResponse(file.getFileName(), file.getFileSize()))
                .collect(Collectors.toList());

//        return fileRepository.findFilesByUserIdWithLimit(userAuth.getId(), limit).stream()
//                .map(file -> new FileResponse(file.getFileName(), file.getFileSize()))
//                .collect(Collectors.toList());
    }

    private boolean isFile(Long userId, String fileName) {
        final var fileEntity = fileRepository.findByUserIdAndFileName(userId, fileName);
        return fileEntity.isPresent();
    }

    private UserEntity getUserAuth() {
        return userService.getUserEntityFromUser(userService.getUserAuth());
    }

    private FileEntity createFileEntity(String fileName, MultipartFile file) throws IOException {
        final var userAuth = getUserAuth();
        return FileEntity.builder()
                .fileName(fileName)
                .fileSize(file.getSize())
                .fileType(file.getContentType())
                .fileDateUpdate(LocalDateTime.now())
                .fileByte(file.getBytes())
                .user(userAuth)
                .build();
    }
}
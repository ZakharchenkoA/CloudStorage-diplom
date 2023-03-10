package ru.netology.cloudstorage.controller;

import ru.netology.cloudstorage.dto.request.FileNameRequest;
import ru.netology.cloudstorage.dto.response.FileResponse;
import ru.netology.cloudstorage.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cloud")
public class FileController {

    private final FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<Void> uploadFile(@RequestParam("filename") String fileName,
                                           @RequestPart("file") MultipartFile file) throws IOException {
        fileService.uploadFile(fileName, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestParam("filename") String fileName) {
        fileService.deleteFile(fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/file", produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String fileName) {
        final var fileEntity = fileService.downloadFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                .body(fileEntity.getFileByte());
    }

    @PutMapping("/file")
    public ResponseEntity<Void> editFileName(@RequestParam("filename") String fileName,
                                             @RequestBody FileNameRequest fileNameRequest) {
        fileService.editFileName(fileName, fileNameRequest.getFileName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileResponse>> getAllFiles(@RequestParam("limit") Integer limit) {
        return new ResponseEntity<>(fileService.getAllFiles(limit), HttpStatus.OK);

//        List<FileResponse> listFile = fileService.getAllFiles(limit);
//        return ResponseEntity.ok().body(listFile);
    }
}
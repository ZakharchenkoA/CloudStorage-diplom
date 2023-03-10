package ru.netology.cloudstorage.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileResponse {

    @JsonProperty("filename")
    private String fileName;
    private Long size;
}
package ru.netology.cloudstorage.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessageResponse {

    private String message;
    private Integer id;
}
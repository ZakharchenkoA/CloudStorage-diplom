package ru.netology.cloudstorage.utils;

public class ErrorMessages {

    public static final String UNAUTHORIZED_ERROR_MESSAGE =
            "Для доступа к этому ресурсу требуется полная аутентификация.";
    public static final String BAD_CREDENTIALS_MESSAGE = "Ошибка ввода данных. Неверные учётные данные.";
    public static final String FILE_NAME_NOT_UNIQUE_MESSAGE =
            "Ошибка ввода данных. Файл с таким именем уже есть в облаке.";
    public static final String FILE_NOT_FOUND_MESSAGE = "Ошибка ввода данных. Файл с таким именем не найден.";
    public static final String NEW_FILE_NAME_UNKNOWN_MESSAGE = "Ошибка ввода данных. Отсутствует новое имя файла.";
    public static final String NEW_FILE_NAME_NOT_UNIQUE_MESSAGE =
            "Ошибка ввода данных. Новое имя файла совпадает с именем файла в облаке.";
    public static final String SERVER_ERROR_MESSAGE =
            "Ошибка сервера. Попробуйте повторить операцию через какое-то время.";
}
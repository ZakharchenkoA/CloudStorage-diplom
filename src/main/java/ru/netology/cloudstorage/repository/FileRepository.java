package ru.netology.cloudstorage.repository;

import ru.netology.cloudstorage.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

    void deleteFileByUserIdAndFileName(Long userId, String fileName);

    Optional<FileEntity> findByUserIdAndFileName(Long userId, String fileName);

    @Modifying
    @Query(value = "update files f set f.file_name = ?1 where f.file_name = ?2 and f.user_Id = ?3", nativeQuery = true)
    void editFileNameByUserId(String newName, String fileName, Long userId);

    List<FileEntity> findAllByUserId(Long userId);

//    @Query(value = "select * from files f where f.user_id = ?1 order by f.id desc limit ?2", nativeQuery = true)
//    List<FileEntity> findFilesByUserIdWithLimit(Long userId, int limit);
}
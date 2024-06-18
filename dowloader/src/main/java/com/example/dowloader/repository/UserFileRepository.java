package com.example.dowloader.repository;

import com.example.dowloader.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE UserFile u SET u.status = :status WHERE u.userFileId = :userFileId")
    void updateStatus(Long userFileId, String status);
}

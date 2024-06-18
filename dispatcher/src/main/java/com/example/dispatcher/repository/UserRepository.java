package com.example.dispatcher.repository;

import com.example.dispatcher.entity.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserFile, Long> {

    @Query(value = "SELECT user_file_id, user_id, file_path, status " +
            "FROM (" +
            "    SELECT user_file_id, user_id, file_path, status, " +
            "           ROW_NUMBER() OVER (PARTITION BY user_id ORDER BY file_path) as rn " +
            "    FROM user_files " +
            "    WHERE status = 'ready' " +
            "      AND NOT EXISTS (" +
            "          SELECT 1 FROM user_files sub " +
            "          WHERE sub.user_id = user_files.user_id " +
            "            AND (sub.status = 'pending' OR sub.status = 'process')" +
            "      )" +
            ") ranked_files " +
            "WHERE rn <= :maxRecord", nativeQuery = true)
    List<UserFile> findReadyFilesExcludingPending(@Param("maxRecord") int maxRecord);

    @Query(value = "SELECT user_file_id, user_id, file_path, status " +
            "FROM user_files " +
            "WHERE status = 'ready'", nativeQuery = true)
    List<UserFile> findReadyFiles();


    @Modifying
    @Transactional
    @Query("UPDATE UserFile u SET u.status = :status WHERE u.userFileId = :userFileId")
    void updateStatus(Long userFileId, String status);

}

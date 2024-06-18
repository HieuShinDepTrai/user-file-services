package com.example.dowloader.repository;

import com.example.dowloader.entity.UserFileDownload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFileDownloadRepository extends JpaRepository<UserFileDownload, Long> {
}

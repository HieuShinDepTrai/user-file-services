package com.example.dowloader.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "user_files_download")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFileDownload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userFilesDownload;

    int userId;
    String filePath;

}

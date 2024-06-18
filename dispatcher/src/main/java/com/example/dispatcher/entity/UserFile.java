package com.example.dispatcher.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "user_files")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_file_id")
    Long userFileId;

    @Column(name = "user_id")
    int userId;

    @Column(name = "file_path")
    String filePath;

    @Column(name = "status")
    String status;
}

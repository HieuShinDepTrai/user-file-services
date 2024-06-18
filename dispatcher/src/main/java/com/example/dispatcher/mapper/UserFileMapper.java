package com.example.dispatcher.mapper;

import com.example.dispatcher.dto.UserFileDto;
import com.example.dispatcher.entity.UserFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")

public interface UserFileMapper {

    UserFile toEntity (UserFileDto userFileDto);
    UserFileDto toDto (UserFile userFile);

    List<UserFileDto> toDto (List<UserFile> userFiles);

    List<UserFile> toEntity (List<UserFileDto> userDtoFiles);
}

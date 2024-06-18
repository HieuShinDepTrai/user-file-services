package com.example.dispatcher.service.schedule;

import com.example.dispatcher.entity.UserFile;
import com.example.dispatcher.repository.UserRepository;
import com.example.dispatcher.utils.RedisQueue;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@Slf4j
public class UserFileSchedule {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisQueue redisQueue;

    @Value("${userfile.max.records}")
    int maxRecord;


    @Scheduled(fixedRate = 3000) // Schedule to run every 5 seconds
    @Transactional
    public void loadDataToRedis() {
        List<UserFile> userFiles = userRepository.findReadyFilesExcludingPending(maxRecord);
        for (UserFile userFile : userFiles) {
            redisQueue.push(userFile);
            System.out.println("Day thanh cong" + userFile);
            userRepository.updateStatus(userFile.getUserFileId(), "pending");
        }
    }
}

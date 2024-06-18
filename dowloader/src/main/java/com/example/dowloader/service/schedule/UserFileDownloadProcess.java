package com.example.dowloader.service.schedule;

import com.example.dowloader.entity.UserFile;
import com.example.dowloader.entity.UserFileDownload;
import com.example.dowloader.repository.UserFileDownloadRepository;
import com.example.dowloader.repository.UserFileRepository;
import com.example.dowloader.utils.RedisQueue;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFileDownloadProcess {

    @Autowired
    UserFileDownloadRepository userFileDownloadRepository;

    @Autowired
    UserFileRepository userFileRepository;

    @Autowired
    RedisQueue redisQueue;

    @Autowired
    ExecutorService executorService;

    @Value("${number.elements.from.queue}")
    int elementFromQueue;

    @Scheduled(fixedRate = 3000)
    public void processQueue() {
        List<String> listUserFile = redisQueue.getElementsFromQueue(elementFromQueue);
        for (String element : listUserFile) {
            executorService.submit(() -> processElement(element));
            redisQueue.removeElementFromQueue(element);
        }
    }

    private void processElement(String element) {

        try {
            UserFile userFile = redisQueue.convertJsonToUserFile(element);
            UserFileDownload userFileDownload = UserFileDownload.builder()
                    .userId(userFile.getUserId())
                    .filePath(userFile.getFilePath())
                    .build();
            // Process and save to Database 2
            userFileRepository.updateStatus(userFile.getUserFileId(), "process");
            userFileDownloadRepository.save(userFileDownload);

            //change elm to done
            userFileRepository.updateStatus(userFile.getUserFileId(), "done");
        } catch (Exception e) {
            log.error("Failed to process element: {}", element, e);
            // Optionally update status to 'fail' and handle error
            try {
                UserFile userFile = redisQueue.convertJsonToUserFile(element);
                userFileRepository.updateStatus(userFile.getUserFileId(), "fail");
            } catch (Exception ex) {
                log.error("Failed to update status to fail for element: {}", element, ex);
            }
        }
    }

}

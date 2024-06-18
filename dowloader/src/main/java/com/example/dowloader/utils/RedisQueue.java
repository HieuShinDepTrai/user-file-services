package com.example.dowloader.utils;

import com.example.dowloader.entity.UserFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisQueue {

    @Value("${redis.queueName}")
    String queueName;

    @Autowired
    final JedisPool jedisPool;

    @Autowired
    final ObjectMapper objectMapper;

    public List<String> getElementsFromQueue(int count) {
        List<String> elements = new ArrayList<>();
        try (Jedis jedis = jedisPool.getResource()) {
            for (int i = 0; i < count; i++) {
                List<String> elementWithKey = jedis.blpop(5, queueName); // Blocking pop
                if (elementWithKey != null && !elementWithKey.isEmpty()) {
                    String element = elementWithKey.get(1); // The value is the second element in the list
                    elements.add(element);
                } else {
                    break; // If the queue is empty, exit the loop
                }
            }
        } catch (JedisException e) {
            log.error("Failed to get elements from queue", e);
            throw e;
        }
        return elements;
    }

    public UserFile convertJsonToUserFile(String json) throws JsonProcessingException {
        return objectMapper.readValue(json, UserFile.class);
    }

    public void removeElementFromQueue(String element) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lrem(queueName, 1, element);
            log.info("Removed element from queue: {}", element);
        } catch (JedisException e) {
            log.error("Failed to remove element from queue", e);
            throw e;
        }
    }

    @PostConstruct
    private void init() {
        log.info("Initialized RedisQueue with queueName: {}", queueName);
    }
}

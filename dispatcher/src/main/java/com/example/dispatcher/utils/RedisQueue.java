package com.example.dispatcher.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import org.springframework.beans.factory.annotation.Value;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisQueue {
    @Value("${redis.queueName}")
    String queueName;

    @Value("${redis.host}")
    String host;

    @Value("${redis.port}")
    int port;

    Jedis jedis;
    ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        this.jedis = new Jedis(host, port);
        this.objectMapper = new ObjectMapper();
        log.info("Initialized RedisQueue with queueName: {}, host: {}, port: {}", queueName, host, port);
    }

    public <T> void push(T message) {
        try {
            String messageJson = objectMapper.writeValueAsString(message);
            jedis.rpush(queueName, messageJson);
        }catch (Exception e) {
            log.error("Failed to convert json " + e.getMessage());
        }
    }

    public void close() {
        if (jedis != null) {
            jedis.close();
        }
    }
}

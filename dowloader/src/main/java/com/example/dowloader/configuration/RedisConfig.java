package com.example.dowloader.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10); // Max total connections
        poolConfig.setMaxIdle(5);   // Max idle connections
        poolConfig.setMinIdle(1);   // Min idle connections
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setTimeBetweenEvictionRunsMillis(30000); // 30 seconds
        poolConfig.setMinEvictableIdleTimeMillis(60000);    // 60 seconds

        // Disable JMX explicitly
        poolConfig.setJmxEnabled(false);

        // Create JedisPool with correct parameters
        return new JedisPool(poolConfig, "localhost", 6379, 2000, null, 0, false);
    }
}

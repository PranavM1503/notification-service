package com.assignment.notification.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Configuration
public class CacheConfig {

    @Bean
    public Jedis jedis(){
        return new Jedis();
    }
}

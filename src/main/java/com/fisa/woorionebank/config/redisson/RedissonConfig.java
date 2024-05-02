//package com.fisa.woorionebank.config.redisson;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///*
// * RedissonClient를 사용하기 위해 Config 설정을 빈으로 등록
// */
//@Configuration
//public class RedissonConfig {
//
//    //Redis 호스트와 포트 정보를 읽어오는데 사용
//    @Value("${spring.redis.host}")
//    private String redisHost;
//    @Value("${spring.redis.port}")
//    private int redisPort;
//
//    private static final String REDISSON_HOST_PREFIX = "redis://";
//
//    @Bean
//    public RedissonClient redissonClient() {
//        RedissonClient redisson = null;
//        Config config = new Config();
//        // 단일 서버 모드로 설정되었으며, Redis 호스트와 포트 정보가 설정
//        config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + redisHost + ":" + redisPort);
//        redisson = Redisson.create(config);
//        return redisson;
//    }
//}

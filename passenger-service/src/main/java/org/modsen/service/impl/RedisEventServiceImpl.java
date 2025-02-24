package org.modsen.service.impl;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modsen.service.RedisEventService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisEventServiceImpl implements RedisEventService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void addEventId(String eventId) {
        redisTemplate.opsForValue().set(eventId, "exists", 3, TimeUnit.DAYS);
        log.info("Redis Event Service. Event with id {} stored to redis", eventId);
    }

    @Override
    public boolean existsByEventId(String eventId) {
        log.info("Redis Event Service. Check event existence by id {}", eventId);
        return Boolean.TRUE.equals(redisTemplate.hasKey(eventId));
    }

}

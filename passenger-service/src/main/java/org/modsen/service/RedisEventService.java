package org.modsen.service;

public interface RedisEventService {

    void addEventId(String eventId);

    boolean existsByEventId(String eventId);

}

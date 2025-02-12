package org.modsen.repository;

import org.modsen.entity.Outbox;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OutboxRepository extends MongoRepository<Outbox, Integer> {

    Page<Outbox> findAllByOrderByOutboxIdAsc(Pageable pageable);

}

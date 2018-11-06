package com.example.ping.repository;

import com.example.ping.repository.entity.PingEntity;
import org.springframework.data.repository.CrudRepository;

public interface PingRepository extends CrudRepository<PingEntity, Long> {

    PingEntity findByUuid(String uuid);

}

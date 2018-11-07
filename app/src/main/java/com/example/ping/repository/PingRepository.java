package com.example.ping.repository;

import java.util.Optional;

import com.example.ping.repository.entity.PingEntity;
import org.springframework.data.repository.CrudRepository;

public interface PingRepository extends CrudRepository<PingEntity, Long> {

    Optional<PingEntity> findByUuid(String uuid);

}

package com.example.ping.repository;

import java.util.List;
import java.util.Optional;

import com.example.ping.repository.entity.MeasurementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MeasurementRepository extends CrudRepository<MeasurementEntity, Long> {

    Optional<MeasurementEntity> findByUuid(String uuid);

    @Query(value = "SELECT * FROM MEASUREMENT ORDER BY response DESC LIMIT 10",
        countQuery = "SELECT count(*) FROM MEASUREMENT ORDER BY response DESC LIMIT 10",
        nativeQuery = true)
    Optional<List<MeasurementEntity>> findByLimit();

}

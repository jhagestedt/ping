package com.example.ping.rest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.Measurement;
import com.example.MeasurementApi;
import com.example.ping.repository.MeasurementRepository;
import com.example.ping.repository.entity.MeasurementEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MeasurementsRest implements MeasurementApi {

    @Autowired
    private MeasurementRepository repository;

    @Override
    public List<Measurement> measurements() {
        Optional<List<MeasurementEntity>> optional = repository.findByLimit();
        if (!optional.isPresent()) {
            return Collections.emptyList();
        }
        return optional.get().stream().map(entity -> new Measurement()
            .setUuid(entity.getUuid())
            .setProtocol(entity.getProtocol())
            .setRequest(entity.getRequest())
            .setResponse(entity.getResponse())
            .setDuration(entity.getDuration())
        ).collect(Collectors.toList());
    }
}

package com.example;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

public interface MeasurementApi {

    @GetMapping(
        path = "/api/measurements",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    List<Measurement> measurements();

}

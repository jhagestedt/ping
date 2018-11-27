package com.example;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

public interface CacheApi {

    @GetMapping(
        path = "/api/cache",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    Cache get();

    @PutMapping(
        path = "/api/cache",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    Cache update();

    @DeleteMapping(
        path = "/api/cache",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    Cache delete();

}

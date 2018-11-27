package com.example.ping.rest;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CacheRest {

    private Data data = new Data();

    @Cacheable(value = "cache-data")
    @GetMapping(
        path = "/api/cache",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Data get() {
        data.number++;
        return data;
    }

    @CachePut(value = "cache-data")
    @PutMapping(
        path = "/api/cache",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Data update() {
        data.number++;
        return data;
    }

    @CacheEvict(value = "cache-data")
    @DeleteMapping(
        path = "/api/cache",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Data delete() {
        return data;
    }

    @Getter
    @Setter
    public static class Data {

        private Long number = 0L;

    }

}

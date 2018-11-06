package com.example.ping.client;

import com.example.pong.PongApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "pong")
public interface PongClient extends PongApi {

}

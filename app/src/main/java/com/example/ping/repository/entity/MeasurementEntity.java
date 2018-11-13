package com.example.ping.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.Protocol;

@Entity
@Table(name = "measurement")
public class MeasurementEntity {

    @Id
    @Column(name = "id",
        unique = true,
        nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid",
        unique = true,
        nullable = false)
    private String uuid;

    @Column(name = "protocol",
        nullable = false)
    private Protocol protocol;

    @Column(name = "request")
    private Long request;

    @Column(name = "response")
    private Long response;

    public String getUuid() {
        return uuid;
    }

    public MeasurementEntity setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public MeasurementEntity setProtocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public Long getRequest() {
        return request;
    }

    public MeasurementEntity setRequest(Long request) {
        this.request = request;
        return this;
    }

    public Long getResponse() {
        return response;
    }

    public MeasurementEntity setResponse(Long response) {
        this.response = response;
        return this;
    }

    public Long getDuration() {
        return this.response - this.request;
    }
}

package com.minimart.logger.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Logger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime timestamp;

    private String principle;

    private String operation;

    public Logger() {
    }

    public Logger(LocalDateTime timestamp, String principle, String operation) {
        this.timestamp = timestamp;
        this.principle = principle;
        this.operation = operation;
    }

}

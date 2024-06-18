package com.minimart.logger.service;

import com.minimart.logger.entity.Logger;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class LoggerServiceImpl implements LoggerService {

    @Autowired
    private final EntityManager entityManager;

    @Transactional
    public void logOperation(String principle, String operation) {
        Logger logger = new Logger(LocalDateTime.now(), principle, operation);
        entityManager.persist(logger);
    }
}

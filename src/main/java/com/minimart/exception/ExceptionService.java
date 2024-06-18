package com.minimart.exception;

import com.minimart.exception.entity.ExceptionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ExceptionService {

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

    public void logException(Exception ex, String transactionId, String principal, String operation, String message) {
        ExceptionLog exceptionLog = new ExceptionLog();
        exceptionLog.setTransactionId(transactionId);
        exceptionLog.setTimestamp(LocalDateTime.now());
        exceptionLog.setPrincipal(principal);
        exceptionLog.setOperation(operation);
        exceptionLog.setExceptionType(ex.getClass().getName());
        exceptionLog.setMessage(message);
        exceptionLogRepository.save(exceptionLog);
    }
}

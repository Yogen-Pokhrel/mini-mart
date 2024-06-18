package com.minimart.aop;

import com.minimart.common.ApiResponse;
import com.minimart.exception.ExceptionService;
import com.minimart.logger.service.LoggerService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Aspect
@Component
public class ExecutionTimeAspect {

    @Autowired
    private LoggerService loggerService;

    /*TODO: add a valid principle */
    private static final String PRINCIPLE = "SOME_PRINCIPLE";

    @Autowired
    private ExceptionService exceptionService;

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restControllerMethods() {
    }

    private String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    @Around("restControllerMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        // LocalDateTime startTime = LocalDateTime.now();

        // Log method entry
        //loggerService.logOperation(PRINCIPLE, "Entering " + methodName + " at " + startTime);

        long start = System.currentTimeMillis();
        Object proceed = null;
        try {
            proceed = joinPoint.proceed();
        }catch (Exception e) {
            exceptionService.logException(e, generateTransactionId(), PRINCIPLE, methodName, e.getMessage());
            proceed = new ResponseEntity<>(ApiResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        finally {
            //long executionTime = System.currentTimeMillis() - start;
            //LocalDateTime endTime = LocalDateTime.now();

            //Log method exit
            //loggerService.logOperation(PRINCIPLE, "Exiting " + methodName + " at " + endTime + ". Execution time: " + executionTime + "ms");
        }
        return proceed;
    }
}

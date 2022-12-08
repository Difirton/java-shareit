package ru.practicum.shareit.common.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class LogAudit {

    @Pointcut("execution(* ru.practicum.shareit..*Controller.create*(..))")
    public void callControllerCreate() {
    }

    @Before("callControllerCreate()")
    public void beforeCallCreateMethod(JoinPoint jp) {
        String args = Arrays.toString(jp.getArgs());
        log.info("Request to create new " + args.substring(1, args.length() - 1));
    }

    @Pointcut("execution(* ru.practicum.shareit..*Controller.update*(..))")
    public void callControllerUpdate() {
    }

    @Before("callControllerUpdate()")
    public void beforeCallUpdateMethod(JoinPoint jp) {
        String args = Arrays.toString(jp.getArgs());
        log.info("Request to update " + args.substring(1, args.length() - 1));
    }

    @Pointcut("execution(* ru.practicum.shareit..*Controller.delete*(..))")
    public void callControllerDelete() {
    }

    @Before("callControllerDelete()")
    public void beforeCallDeleteMethod(JoinPoint jp) {
        String args = Arrays.toString(jp.getArgs());
        log.info("Request to delete " + jp.getSignature().getName().substring(6) +
                " with parameters: " + args);
    }
}

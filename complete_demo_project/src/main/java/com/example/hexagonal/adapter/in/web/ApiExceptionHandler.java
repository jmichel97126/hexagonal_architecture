package com.example.hexagonal.adapter.in.web;

import com.example.hexagonal.application.exception.OrderNotFoundException;
import com.example.hexagonal.domain.exception.BusinessRuleViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(OrderNotFoundException.class)
    public ProblemDetail handleNotFound(OrderNotFoundException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Order Not Found");
        return problem;
    }

    @ExceptionHandler({BusinessRuleViolationException.class, IllegalArgumentException.class})
    public ProblemDetail handleDomainErrors(RuntimeException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setTitle("Business Validation Error");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "invalid request payload");
        problem.setTitle("Request Validation Error");
        return problem;
    }
}

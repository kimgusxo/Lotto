package com.example.lotto.error;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception ex) {
        CustomException customEx = new CustomException(ex);
        return new ResponseEntity<>(ErrorDTO.of(customEx.getErrorCode()), customEx.getStatus());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorDTO> handleCustomException(CustomException ex) {
        return new ResponseEntity<>(ErrorDTO.of(ex.getErrorCode()), ex.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDTO> handleConstraintViolationException(ConstraintViolationException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ErrorCode.VALIDATION);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ErrorCode.VALIDATION);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidException(HandlerMethodValidationException ex) {
        ErrorDTO errorDTO = ErrorDTO.of(ErrorCode.VALIDATION);
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

}

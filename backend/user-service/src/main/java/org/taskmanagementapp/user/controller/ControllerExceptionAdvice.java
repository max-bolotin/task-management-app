package org.taskmanagementapp.user.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.taskmanagementapp.user.dto.ErrorRestResponseDto;
import org.taskmanagementapp.user.exception.UserAuthenticationException;

@ControllerAdvice
public class ControllerExceptionAdvice {

  @ExceptionHandler(UserAuthenticationException.class)
  public final ResponseEntity<ErrorRestResponseDto> handleException(
      final UserAuthenticationException uae) {
    final List<String> details = new ArrayList<>();
    details.add(uae.getMessage());
    final ErrorRestResponseDto error = ErrorRestResponseDto.of(HttpStatus.BAD_REQUEST.toString(),
        details);
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
}

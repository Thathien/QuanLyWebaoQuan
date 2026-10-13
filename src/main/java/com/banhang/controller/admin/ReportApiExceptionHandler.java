package com.banhang.controller.admin;

import com.banhang.dto.report.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice(
    assignableTypes = {
      BaoCaoAdminController.class,
      KhoAdminController.class,
      OrderAccountingAdminController.class
    })
public class ReportApiExceptionHandler {
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ApiErrorResponse> handleStatus(
      ResponseStatusException exception, HttpServletRequest request) {
    HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
    return response(status, exception.getReason(), request);
  }

  @ExceptionHandler({
    IllegalArgumentException.class,
    MethodArgumentTypeMismatchException.class,
    MethodArgumentNotValidException.class
  })
  public ResponseEntity<ApiErrorResponse> handleInvalidInput(
      Exception exception, HttpServletRequest request) {
    String message =
        exception instanceof IllegalArgumentException && exception.getMessage() != null
            ? exception.getMessage()
            : "Tham số yêu cầu không hợp lệ.";
    return response(HttpStatus.BAD_REQUEST, message, request);
  }

  private ResponseEntity<ApiErrorResponse> response(
      HttpStatus status, String message, HttpServletRequest request) {
    return ResponseEntity.status(status)
        .body(
            new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()));
  }
}

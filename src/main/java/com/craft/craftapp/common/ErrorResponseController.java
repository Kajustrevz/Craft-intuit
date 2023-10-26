package com.craft.craftapp.common;

import com.craft.craftapp.common.dto.ApiResponse;
import com.craft.craftapp.common.dto.ApiStatus;
import com.craft.craftapp.exception.ResourceNotFoundException;
import com.craft.craftapp.exception.UserAlreadyExistsException;
import com.craft.craftapp.exception.UserNotAuthorizedForOperationException;
import lombok.ToString;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ErrorResponseController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    protected ResponseEntity<ApiResponse> handleUserAlreadyExistException(UserAlreadyExistsException ex){
        return buildErrorResponseEntity(buildErrorResponse(ex), HttpStatus.TOO_MANY_REQUESTS);
    }
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ApiResponse> handleAuthenticationException(AuthenticationException ex){
        return buildErrorResponseEntity(buildErrorResponse(ex), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
        return buildErrorResponseEntity(buildErrorResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotAuthorizedForOperationException.class)
    protected ResponseEntity<ApiResponse> handleUserNotAuthorizedForOperationException(UserNotAuthorizedForOperationException ex){
        return buildErrorResponseEntity(buildErrorResponse(ex), HttpStatus.UNAUTHORIZED);
    }

    private ApiResponse buildErrorResponse(Exception ex){
        return ApiResponse.builder().apiStatus(ApiStatus.ERROR).message(
                ex.getMessage()).build();
    }
    private ResponseEntity<ApiResponse> buildErrorResponseEntity(ApiResponse body, HttpStatus httpStatus) {
        return new ResponseEntity<ApiResponse>(body, httpStatus);
    }
}

package com.example.com.rakesh.smart_task_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	//Handle Runtime Exception (Task Not found)
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handlerRuntimeException(RuntimeException ex){
		ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}
	// Handle Validation Errors (@Valid)
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
		public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex){
			
			FieldError fieldError = ex.getBindingResult().getFieldError();
			ErrorResponse error = new ErrorResponse(fieldError.getDefaultMessage(),HttpStatus.BAD_REQUEST.value());
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
			
		}
	//All other Exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex){
		
		ErrorResponse error = new ErrorResponse(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR.value());
		
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}
	
	
	

}

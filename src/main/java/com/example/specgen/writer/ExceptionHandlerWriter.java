package com.example.specgen.writer;

import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.example.specgen.generator.ExceptionHandlerGenerator;

public class ExceptionHandlerWriter {

    private final StringBuilder stringFile;

    public ExceptionHandlerWriter(StringBuilder stringFile){
		this.stringFile = stringFile;
	}

    public void create(String packageName){
        this.stringFile.append("""
                package %s;

                import org.springframework.http.HttpStatus;
                import org.springframework.http.ResponseEntity;
                import org.springframework.web.bind.MethodArgumentNotValidException;
                import org.springframework.web.bind.annotation.ExceptionHandler;
                import org.springframework.web.bind.annotation.RestControllerAdvice;
                import jakarta.persistence.EntityNotFoundException;

                import java.util.HashMap;
                import java.util.Map;

                @RestControllerAdvice
                public class GlobalExceptionHandler {

                    @ExceptionHandler(EntityNotFoundException.class)
                    public ResponseEntity<Map<String, String>> handleNotFound(EntityNotFoundException ex) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", ex.getMessage());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
                    }

                    @ExceptionHandler(MethodArgumentNotValidException.class)
                    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
                        Map<String, String> errors = new HashMap<>();
                        ex.getBindingResult().getFieldErrors()
                            .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
                        return ResponseEntity.badRequest().body(errors);
                    }

                    @ExceptionHandler(Exception.class)
                    public ResponseEntity<Map<String, String>> handleGeneric(Exception ex) {
                        Map<String, String> error = new HashMap<>();
                        error.put("error", "An unexpected error occurred");
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
                    }
                }
                """.formatted(packageName));
    }

    public String getStringFile(){
		return stringFile.toString();
	}
}
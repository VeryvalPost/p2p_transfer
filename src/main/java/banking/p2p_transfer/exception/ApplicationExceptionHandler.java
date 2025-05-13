package banking.p2p_transfer.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        log.error("Ошибка валидации: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<String> handleDatabaseException(DatabaseException ex) {
        log.error("Ошибка базы данных: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка базы данных: " + ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        log.error("Пользователь не найден: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден: {}" +  ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("Неизвестная ошибка: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Произошла ошибка. Пожалуйста, попробуйте позже.");
    }

}

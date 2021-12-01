package ru.gov.mcx.gispz.subject;


import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.gov.mcx.gispz.commons.json.ErrorResponse;


/**
 * Обработка ошибок JSON
 *
 * @author mterehov
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApplicationExceptionHandler {
    /** Logger */
    private static final Logger log = LogManager.getLogger();


    /**
     * Ошибка формата JSON:  отсутствие обязательных аргументов, отсутствие request body.
     *
     * @param e -
     * @return -
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        // трассировка для ошибки
        log.fatal("{}", e.getMessage(), e);

        Throwable t = ExceptionUtils.getRootCause(e);

        return ResponseEntity
            .badRequest()
            .body(
                ErrorResponse.builder()
                    .cause(e)
                    .message(t.getMessage())
                    .status(HttpStatus.BAD_REQUEST)
                    .build()
            );
    }
}

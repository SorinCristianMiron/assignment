package learning.assignment.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.LazyInitializationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
//public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // --------- 400: Body validation (@Valid @RequestBody) ----------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        // field errors
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }

        // global/object errors
        ex.getBindingResult().getGlobalErrors()
                .forEach(ge -> errors.put(ge.getObjectName(), ge.getDefaultMessage()));

        return Map.of("message", "Validation failed", "errors", errors);
    }

    // --------- 400: Param validation (@Validated + @RequestParam/@PathVariable) ----------
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            errors.put(v.getPropertyPath().toString(), v.getMessage());
        }
        return Map.of("message", "Validation failed", "errors", errors);
    }

    // --------- 400: Binding on query/form/model attributes ----------
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBindException(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));
        return Map.of("message", "Validation failed", "errors", errors);
    }

    // --------- 400: JSON invalid / enum/date invalid / malformed body ----------
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return Map.of("message", "Request body invalid sau nu poate fi deserializat.");
    }

    // --------- 400: Missing / wrong type request params ----------
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMissingRequestParam(MissingServletRequestParameterException ex) {
        return Map.of("message", "Lipsește parametrul obligatoriu: " + ex.getParameterName());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return Map.of("message", "Parametrul '" + ex.getName() + "' are valoare invalidă: " + ex.getValue());
    }

    @ExceptionHandler(MissingPathVariableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMissingPathVariable(MissingPathVariableException ex) {
        return Map.of("message", "Lipsește path variable: " + ex.getVariableName());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleIllegalArgument(IllegalArgumentException ex) {
        return Map.of("message", ex.getMessage());
    }

    // --------- 404: Not found ----------
    @ExceptionHandler({ResourceNotFoundException.class, UsernameNotFoundException.class, EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(Exception ex) {
        if (ex instanceof EmptyResultDataAccessException) {
            return Map.of("message", "Resource not found");
        }
        return Map.of("message", ex.getMessage());
    }

    // --------- 401 / 403: Security ----------
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleAuthenticationException(AuthenticationException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Map<String, String> handleAuthMissing(AuthenticationCredentialsNotFoundException ex) {
        return Map.of("message", "Nu ești autentificat.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleAccessDeniedException(AccessDeniedException ex) {
        return Map.of("message", "Nu ai permisiuni pentru această operație.");
    }

    // --------- 409: DB constraints / duplicates / FK constraints ----------
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return Map.of("message", "Operația nu poate fi efectuată din cauza unei constrângeri de date (duplicat/relații).");
    }

    // --------- 400: JPA validation at commit (TransactionSystemException -> ConstraintViolationException) ----------
    @ExceptionHandler(TransactionSystemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleTransactionSystemException(TransactionSystemException ex) {
        Throwable root = ex.getRootCause();
        if (root instanceof jakarta.validation.ConstraintViolationException cve) {
            Map<String, String> errors = new HashMap<>();
            cve.getConstraintViolations()
                    .forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
            return Map.of("message", "Validation failed", "errors", errors);
        }
        return Map.of("message", "Transaction failed");
    }

    // --------- 415 / 406 / 405: HTTP protocol edgecases ----------
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Map<String, String> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        return Map.of("message", "Content-Type nesuportat. Folosește application/json.");
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Map<String, String> handleNotAcceptable(HttpMediaTypeNotAcceptableException ex) {
        return Map.of("message", "Formatul cerut nu este suportat.");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Map<String, String> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return Map.of("message", "Metoda HTTP nu este suportată pentru acest endpoint.");
    }

    // --------- 500: Lazy loading serialization safety net ----------
    @ExceptionHandler(LazyInitializationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleLazyInit(LazyInitializationException ex) {
        return Map.of("message", "Eroare de mapare a datelor. Folosește DTO-uri / fetch controlat.");
    }

    // --------- 500: dumnezeu cu mila ----------
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGeneric(Exception ex) {
        // log.error("Unhandled exception", ex);
        return Map.of("message", "A apărut o eroare internă.");
    }
}
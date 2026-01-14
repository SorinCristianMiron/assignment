package learning.assignment.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CustomApiResponse<T> {
    private final String guid = UUID.randomUUID().toString();
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String code;
    private final String message;
    private final String path;
    private final String method;
    private final T data;
}

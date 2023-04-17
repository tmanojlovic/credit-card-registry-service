package hr.bank.creditcardregistryservice.exception.handler.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    @JsonFormat
    private HttpStatusCode status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    @JsonFormat
    private List<String> errorMessages;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(List<String> errorMessages, HttpStatusCode status) {
        this();
        this.status = status;
        this.errorMessages = errorMessages;
    }
}
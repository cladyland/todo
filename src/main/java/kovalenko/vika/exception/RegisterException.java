package kovalenko.vika.exception;

import lombok.Getter;

import java.util.Map;

public class RegisterException extends RuntimeException {
    @Getter
    private Map<String, String> errors;

    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(Map<String, String> errors){
        this.errors = errors;
    }
}

package com.polarbear.feigndemo.config.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private Error error;

    public ErrorResponse(Error error) {
        this.error = error;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Error {
        private String type;
        private String message;

        public Error(String type, String message) {
            this.type = type;
            this.message = message;
        }
    }
}

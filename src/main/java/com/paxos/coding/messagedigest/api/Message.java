package com.paxos.coding.messagedigest.api;

/**
 * DTO representing a request/response containing a string message.
 *
 */
public class Message {

    private String message;

    public Message() {}

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * DTO representing an error when a digest cannot be mapped to a message.
     *
     */
    public static class ErrorResponse extends Message {

        private String errorMessage;

        public ErrorResponse() {}

        public ErrorResponse(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}

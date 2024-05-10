package Exceptions;

public class StudentAlreadyExistsException extends Exception {

    public StudentAlreadyExistsException() {
        super("Student already exists");
    }

    public StudentAlreadyExistsException(String message) {
        super(message);
    }

    public StudentAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}


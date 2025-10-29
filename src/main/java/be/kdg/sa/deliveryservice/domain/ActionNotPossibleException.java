package be.kdg.sa.deliveryservice.domain;

public class ActionNotPossibleException extends RuntimeException {
    public ActionNotPossibleException(String message) {
        super(message);
    }
}

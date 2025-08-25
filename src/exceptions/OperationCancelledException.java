package exceptions;

public class OperationCancelledException extends DomainException {
    public OperationCancelledException(String message) { super(message); }
}

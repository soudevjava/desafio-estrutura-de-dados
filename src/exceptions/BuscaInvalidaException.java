package exceptions;

public class BuscaInvalidaException extends RuntimeException {
    public BuscaInvalidaException(String mensagem) {
        super(mensagem);
    }
}

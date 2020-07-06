package javafx.utilities;

/**
 * Classe utilizada para lançar uma exception, quando não for possível gerar um verso.
 */
public class VersoException extends Exception {
    public VersoException(String message) {
        super(message, null);
    }

    public VersoException() {
        super();
    }
}

package javafx.utilities;

/**
 * Classe utilizada para lançar uma exception, quando não for possível gerar um poema.
 */
public class PoemaException extends Exception {
    public PoemaException(String message) {
        super(message, null);
    }
}

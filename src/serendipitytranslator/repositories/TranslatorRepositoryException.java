/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package serendipitytranslator.repositories;

/**
 *
 * @author Vláďa
 */
public class TranslatorRepositoryException extends Exception {

    /**
     * Creates a new instance of
     * <code>TranslatorRepositoryException</code> without detail message.
     */
    public TranslatorRepositoryException() {
    }

    /**
     * Constructs an instance of <code>TranslatorRepositoryException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public TranslatorRepositoryException(String msg) {
        super(msg);
    }
}

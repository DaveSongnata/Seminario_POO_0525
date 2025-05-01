package model;

/**
 * Exceção lançada quando há tentativa de cadastrar um produto com código duplicado.
 */
public class CodigoDuplicadoException extends Exception {
    
    public CodigoDuplicadoException(String msg) {
        super(msg);
    }
} 
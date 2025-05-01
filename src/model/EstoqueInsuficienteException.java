package model;

/**
 * Exceção lançada quando o estoque de um produto é insuficiente para a venda.
 */
public class EstoqueInsuficienteException extends Exception {
    
    public EstoqueInsuficienteException(String msg) {
        super(msg);
    }
} 
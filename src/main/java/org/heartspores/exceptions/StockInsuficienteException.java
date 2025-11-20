package org.heartspores.exceptions;

public class StockInsuficienteException extends RuntimeException {
    public StockInsuficienteException(String msg) {
        super(msg);
    }
}
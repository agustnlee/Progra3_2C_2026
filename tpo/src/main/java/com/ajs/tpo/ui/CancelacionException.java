package com.ajs.tpo.ui;

// Se lanza cuando el admin ingresa "-" para cancelar la operacion en curso
public class CancelacionException extends RuntimeException {
    public CancelacionException() {
        super("Operacion cancelada");
    }
}
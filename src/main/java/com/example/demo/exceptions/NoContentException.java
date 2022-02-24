package com.example.demo.exceptions;

public class NoContentException extends RuntimeException{
    public NoContentException(String mensaje){
        super(mensaje);
    }
}

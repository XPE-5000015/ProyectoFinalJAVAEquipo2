package com.example.demo.exceptions;

public class ConflictException extends RuntimeException{
    public ConflictException(String mensaje){
        super(mensaje);
    }
}

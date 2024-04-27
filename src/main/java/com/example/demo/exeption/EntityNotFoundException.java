package com.example.demo.exeption;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String messege) {
        super(messege);
    }
}

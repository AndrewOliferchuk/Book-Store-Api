package com.example.demo.model;

public enum Status {
    PENDING,
    PROCESSING,
    COMPLETED,
    CANCELLED,
    DELIVERED;

    public static Status getType(String type) {
        for (Status status : Status.values()) {
            if (status.name().equals(type)) {
                return status;
            }
        }
        return null;
    }
}

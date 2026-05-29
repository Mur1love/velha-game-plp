package com.velha.model;

public enum Symbol {
    X, O, EMPTY;

    @Override
    public String toString() {
        return switch (this) {
            case X -> "X";
            case O -> "O";
            case EMPTY -> "";
        };
    }

    public Symbol opposite() {
        return switch (this) {
            case X -> O;
            case O -> X;
            case EMPTY -> EMPTY;
        };
    }
}
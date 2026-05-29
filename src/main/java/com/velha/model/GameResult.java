package com.velha.model;

public enum GameResult {
    WIN_X, WIN_O, DRAW, ONGOING;

    public boolean isFinished() {
        return this != ONGOING;
    }

    public String getMessage() {
        return switch (this) {
            case WIN_X -> "Jogador X venceu!";
            case WIN_O -> "Jogador O venceu!";
            case DRAW -> "Empate!";
            case ONGOING -> "Jogo em andamento";
        };
    }
}
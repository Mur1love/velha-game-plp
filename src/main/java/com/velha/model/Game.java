package com.velha.model;

public class Game {

    private final Board board;
    private final Player[] players;
    private int currentPlayerIndex;
    private boolean gameOver;
    private final int timeLimit;
    private GameResult result;
    private String winnerMessage;

    public Game(int timeLimit) {
        this.board = new Board();
        this.players = new Player[]{
            new Player("Jogador X", Symbol.X),
            new Player("Jogador O", Symbol.O)
        };
        this.currentPlayerIndex = 0;
        this.gameOver = false;
        this.timeLimit = timeLimit;
        this.result = GameResult.ONGOING;
        this.winnerMessage = "";
    }

    public boolean makeMove(int position) {
        if (gameOver) {
            return false;
        }

        boolean success = board.makeMove(position, getCurrentPlayer().getSymbol());
        if (!success) {
            return false;
        }

        result = board.checkResult();
        if (result != GameResult.ONGOING) {
            gameOver = true;
            winnerMessage = result.getMessage();
        } else {
            switchPlayer();
        }
        return true;
    }

    public void switchPlayer() {
        currentPlayerIndex = 1 - currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public GameResult getResult() {
        return result;
    }

    public Board getBoard() {
        return board;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public String getWinnerMessage() {
        if (gameOver) {
            return winnerMessage;
        }
        return "";
    }

    public void forfeitDueToTimeout() {
        gameOver = true;
        Symbol winnerSymbol = getCurrentPlayer().getSymbol().opposite();
        result = (winnerSymbol == Symbol.X) ? GameResult.WIN_X : GameResult.WIN_O;
        winnerMessage = result.getMessage().replace("!", "! (Tempo esgotado)");
    }

    public void reset() {
        board.reset();
        currentPlayerIndex = 0;
        gameOver = false;
        result = GameResult.ONGOING;
        winnerMessage = "";
    }
}
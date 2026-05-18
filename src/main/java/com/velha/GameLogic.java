package com.velha;

public class GameLogic {

    private static final char[] board = new char[9];
    private static char currentPlayer = 'X';
    private static boolean gameOver = false;
    private static int timeLimit = 10;
    private static String winnerMessage = "";

    private static final int[][] WIN_COMBOS = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    public static void resetBoard() {
        for (int i = 0; i < 9; i++) {
            board[i] = '\0';
        }
        currentPlayer = 'X';
        gameOver = false;
        winnerMessage = "";
    }

    public static boolean makeMove(int position) {
        if (position < 0 || position > 8) return false;
        if (board[position] != '\0') return false;
        if (gameOver) return false;

        board[position] = currentPlayer;
        return true;
    }

    public static void switchPlayer() {
        if (currentPlayer == 'X') {
            currentPlayer = 'O';
        } else {
            currentPlayer = 'X';
        }
    }

    public static char checkWinner() {
        for (int i = 0; i < WIN_COMBOS.length; i++) {
            int a = WIN_COMBOS[i][0];
            int b = WIN_COMBOS[i][1];
            int c = WIN_COMBOS[i][2];

            if (board[a] != '\0' && board[a] == board[b] && board[b] == board[c]) {
                gameOver = true;
                winnerMessage = "Jogador " + board[a] + " venceu!";
                return board[a];
            }
        }
        return '\0';
    }

    public static boolean checkDraw() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == '\0') return false;
        }
        gameOver = true;
        winnerMessage = "Empate!";
        return true;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static char getCurrentPlayer() {
        return currentPlayer;
    }

    public static String getWinnerMessage() {
        return winnerMessage;
    }

    public static int getTimeLimit() {
        return timeLimit;
    }

    public static void setTimeLimit(int seconds) {
        timeLimit = seconds;
    }

    public static void setGameOver(boolean over) {
        gameOver = over;
    }

    public static void setWinnerMessage(String msg) {
        winnerMessage = msg;
    }
}
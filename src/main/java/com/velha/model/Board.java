package com.velha.model;

public class Board {

    private static final int[][] WIN_COMBOS = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
        {0, 4, 8}, {2, 4, 6}
    };

    private final Symbol[] cells = new Symbol[9];

    public Board() {
        reset();
    }

    public boolean makeMove(int position, Symbol symbol) {
        if (position < 0 || position > 8) {
            return false;
        }
        if (cells[position] != Symbol.EMPTY) {
            return false;
        }
        cells[position] = symbol;
        return true;
    }

    public Symbol getCell(int position) {
        return cells[position];
    }

    public GameResult checkResult() {
        for (int[] combo : WIN_COMBOS) {
            Symbol a = cells[combo[0]];
            Symbol b = cells[combo[1]];
            Symbol c = cells[combo[2]];
            if (a != Symbol.EMPTY && a == b && b == c) {
                return a == Symbol.X ? GameResult.WIN_X : GameResult.WIN_O;
            }
        }
        if (isFull()) {
            return GameResult.DRAW;
        }
        return GameResult.ONGOING;
    }

    public boolean isFull() {
        for (Symbol cell : cells) {
            if (cell == Symbol.EMPTY) {
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty(int position) {
        return cells[position] == Symbol.EMPTY;
    }

    public void reset() {
        for (int i = 0; i < cells.length; i++) {
            cells[i] = Symbol.EMPTY;
        }
    }
}
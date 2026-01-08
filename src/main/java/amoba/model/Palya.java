package amoba.model;

import java.util.Arrays;

public class Palya {
    private final int rows;
    private final int cols;
    private final Cella[][] grid;

    public Palya(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cella[rows][cols];
        // Kezdetben minden mező üres
        for (Cella[] row : grid) {
            Arrays.fill(row, Cella.EMPTY);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cella getCell(Pozicio pos) {
        if (pos.row() < 0 || pos.row() >= rows || pos.col() < 0 || pos.col() >= cols) {
            throw new IllegalArgumentException("Érvénytelen pozíció: " + pos);
        }
        return grid[pos.row()][pos.col()];
    }

    public void setCell(Pozicio pos, Cella cella) {
        if (pos.row() < 0 || pos.row() >= rows || pos.col() < 0 || pos.col() >= cols) {
            throw new IllegalArgumentException("Érvénytelen pozíció: " + pos);
        }
        grid[pos.row()][pos.col()] = cella;
    }

    // Ez segít majd kirajzolni a táblát a konzolra
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int j = 0; j < cols; j++) {
            sb.append((char) ('a' + j)).append(" ");
        }
        sb.append("\n");
        for (int i = 0; i < rows; i++) {
            sb.append(i + 1).append(i + 1 < 10 ? " " : "");
            for (int j = 0; j < cols; j++) {
                sb.append(grid[i][j].symbol).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
package amoba.model;

public class Palya {
    private final int rows;
    private final int cols;
    private final Cella[][] cells; // A tábla cellái

    public Palya(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cella[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = Cella.URES;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cella getCell(Pozicio p) {
        return cells[p.row()][p.col()];
    }

    public void setCell(Pozicio p, Cella c) {
        cells[p.row()][p.col()] = c;
    }

    public boolean isOnBoard(Pozicio p) {
        return p.row() >= 0 && p.row() < rows &&
                p.col() >= 0 && p.col() < cols;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int j = 0; j < cols; j++) sb.append((char) ('a' + j)).append(" ");
        sb.append("\n");
        for (int i = 0; i < rows; i++) {
            sb.append((i + 1)).append(i < 9 ? " " : "");
            for (int j = 0; j < cols; j++) {
                sb.append(cells[i][j] == Cella.URES ? "." : cells[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
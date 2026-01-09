package amoba.service;

import amoba.model.Cella;
import amoba.model.Palya;
import amoba.model.Pozicio;
import java.util.Random;

public class Jatek {

    public boolean isValidMove(Palya palya, Pozicio pozicio) {
        if (!palya.isOnBoard(pozicio)) {
            return false;
        }
        return palya.getCell(pozicio) == Cella.URES;

    }

    public void makeMove(Palya palya, Pozicio pozicio, Cella cella) {
        palya.setCell(pozicio, cella);
    }

    public boolean checkWin(Palya palya, Cella jatekos) {
        int rows = palya.getRows();
        int cols = palya.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c <= cols - 5; c++) {
                if (checkLine(palya, r, c, 0, 1, jatekos)) return true;
            }
        }
        for (int r = 0; r <= rows - 5; r++) {
            for (int c = 0; c < cols; c++) {
                if (checkLine(palya, r, c, 1, 0, jatekos)) return true;
            }
        }
        for (int r = 0; r <= rows - 5; r++) {
            for (int c = 0; c <= cols - 5; c++) {
                if (checkLine(palya, r, c, 1, 1, jatekos)) return true;
            }
        }
        for (int r = 4; r < rows; r++) {
            for (int c = 0; c <= cols - 5; c++) {
                if (checkLine(palya, r, c, -1, 1, jatekos)) return true;
            }
        }
        return false;
    }

    private boolean checkLine(Palya palya, int r, int c, int dr, int dc, Cella jatekos) {
        for (int i = 0; i < 5; i++) {
            if (palya.getCell(new Pozicio(r + i * dr, c + i * dc)) != jatekos) {
                return false;
            }
        }
        return true;
    }

    public Pozicio getAiMove(Palya palya) {
        Random rand = new Random();
        int rows = palya.getRows();
        int cols = palya.getCols();

        for (int i = 0; i < 100; i++) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            Pozicio p = new Pozicio(r, c);
            if (isValidMove(palya, p)) {
                return p;
            }
        }
        return null;
    }
}
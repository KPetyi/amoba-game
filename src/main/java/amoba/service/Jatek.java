package amoba.service;

import amoba.model.Cella;
import amoba.model.Palya;
import amoba.model.Pozicio;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jatek {
    private static final Logger logger = LoggerFactory.getLogger(Jatek.class);
    private final Random random = new Random();

    /**
     * Ellenőrzi, hogy a lépés érvényes-e.
     */
    public boolean isValidMove(Palya palya, Pozicio pozicio) {
        // 1. Pályán belül van?
        if (pozicio.row() < 0 || pozicio.row() >= palya.getRows()
                || pozicio.col() < 0 || pozicio.col() >= palya.getCols()) {
            return false;
        }

        // 2. Üres a mező? (feltételezve, hogy a Palya osztályban a metódus neve maradt getCell)
        if (palya.getCell(pozicio) != Cella.EMPTY) {
            logger.warn("Érvénytelen lépés: A mező nem üres: {}", pozicio);
            return false;
        }

        // 3. Szomszédsági szabály
        if (isPalyaEmpty(palya)) {
            return true;
        }

        boolean hasNeighbor = checkNeighbors(palya, pozicio);
        if (!hasNeighbor) {
            logger.warn("Érvénytelen lépés: Nincs szomszédja a jelnek: {}", pozicio);
        }
        return hasNeighbor;
    }

    public void makeMove(Palya palya, Pozicio pozicio, Cella jatekos) {
        palya.setCell(pozicio, jatekos);
        logger.info("Játékos {} lépett ide: {}", jatekos, pozicio);
    }

    public Pozicio getAiMove(Palya palya) {
        int attempts = 1000;
        while (attempts-- > 0) {
            int r = random.nextInt(palya.getRows());
            int c = random.nextInt(palya.getCols());
            Pozicio pozicio = new Pozicio(r, c);

            if (isValidMove(palya, pozicio)) {
                return pozicio;
            }
        }
        return null;
    }

    public boolean checkWin(Palya palya, Cella jatekos) {
        int rows = palya.getRows();
        int cols = palya.getCols();
        char sym = jatekos.symbol;

        // Vízszintes
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c <= cols - 4; c++) {
                if (checkLine(palya, r, c, 0, 1, sym)) return true;
            }
        }
        // Függőleges
        for (int r = 0; r <= rows - 4; r++) {
            for (int c = 0; c < cols; c++) {
                if (checkLine(palya, r, c, 1, 0, sym)) return true;
            }
        }
        // Átlós
        for (int r = 0; r <= rows - 4; r++) {
            for (int c = 0; c <= cols - 4; c++) {
                if (checkLine(palya, r, c, 1, 1, sym)) return true;
            }
        }
        // Átlós (másik irány)
        for (int r = 0; r <= rows - 4; r++) {
            for (int c = 3; c < cols; c++) {
                if (checkLine(palya, r, c, 1, -1, sym)) return true;
            }
        }
        return false;
    }

    private boolean checkLine(Palya p, int r, int c, int dr, int dc, char sym) {
        return p.getCell(new Pozicio(r, c)).symbol == sym
                && p.getCell(new Pozicio(r + dr, c + dc)).symbol == sym
                && p.getCell(new Pozicio(r + 2 * dr, c + 2 * dc)).symbol == sym
                && p.getCell(new Pozicio(r + 3 * dr, c + 3 * dc)).symbol == sym;
    }

    private boolean isPalyaEmpty(Palya palya) {
        for (int i = 0; i < palya.getRows(); i++) {
            for (int j = 0; j < palya.getCols(); j++) {
                if (palya.getCell(new Pozicio(i, j)) != Cella.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkNeighbors(Palya palya, Pozicio pozicio) {
        for (int r = -1; r <= 1; r++) {
            for (int c = -1; c <= 1; c++) {
                if (r == 0 && c == 0) continue;

                int nr = pozicio.row() + r;
                int nc = pozicio.col() + c;

                if (nr >= 0 && nr < palya.getRows() && nc >= 0 && nc < palya.getCols()) {
                    if (palya.getCell(new Pozicio(nr, nc)) != Cella.EMPTY) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
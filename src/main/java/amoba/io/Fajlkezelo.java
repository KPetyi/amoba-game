package amoba.io;

import amoba.model.Cella;
import amoba.model.Palya;
import amoba.model.Pozicio;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Fajlkezelo {
    private static final Logger logger = LoggerFactory.getLogger(Fajlkezelo.class);
    private static final String FILE_NAME = "gamestate.txt";

    public void saveGame(Palya palya) {
        StringBuilder sb = new StringBuilder();
        // Első sor: sorok és oszlopok száma
        sb.append(palya.getRows()).append(" ").append(palya.getCols()).append("\n");

        // Pálya tartalmának mentése
        for (int i = 0; i < palya.getRows(); i++) {
            for (int j = 0; j < palya.getCols(); j++) {
                sb.append(palya.getCell(new Pozicio(i, j)).symbol);
            }
            sb.append("\n");
        }

        try {
            Files.writeString(Path.of(FILE_NAME), sb.toString());
            logger.info("Játék mentve ide: {}", FILE_NAME);
        } catch (IOException e) {
            logger.error("Nem sikerült menteni a játékot", e);
        }
    }

    public Palya loadGame() {
        if (!Files.exists(Path.of(FILE_NAME))) {
            logger.info("Nincs mentett játék, új pálya indul.");
            return new Palya(10, 10);
        }

        try {
            List<String> lines = Files.readAllLines(Path.of(FILE_NAME));
            // Méretek beolvasása az első sorból
            String[] dims = lines.get(0).split(" ");
            int rows = Integer.parseInt(dims[0]);
            int cols = Integer.parseInt(dims[1]);

            Palya palya = new Palya(rows, cols);

            // Pálya feltöltése
            for (int i = 0; i < rows; i++) {
                // +1, mert az első sor a méret
                String line = lines.get(i + 1);
                for (int j = 0; j < cols; j++) {
                    char c = line.charAt(j);
                    Pozicio p = new Pozicio(i, j);

                    if (c == 'X') {
                        palya.setCell(p, Cella.X);
                    } else if (c == 'O') {
                        palya.setCell(p, Cella.O);
                    }
                }
            }
            logger.info("Játék sikeresen betöltve.");
            return palya;
        } catch (Exception e) {
            logger.error("Hiba a betöltéskor, új pálya indul.", e);
            return new Palya(10, 10);
        }
    }
}
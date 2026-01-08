package amoba;

import amoba.io.Fajlkezelo;
import amoba.model.Cella;
import amoba.model.Palya;
import amoba.model.Pozicio;
import amoba.service.Jatek;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Jatek jatekService = new Jatek();
        Fajlkezelo fajlKezelo = new Fajlkezelo();

        System.out.println("Szeretnéd betölteni a korábbi játékot? (i/n)");
        String choice = scanner.nextLine();

        Palya palya;
        if (choice.equalsIgnoreCase("i")) {
            palya = fajlKezelo.loadGame();
        } else {
            palya = new Palya(10, 10);
        }

        boolean running = true;
        while (running) {
            // Pálya kirajzolása
            System.out.println(palya);
            System.out.println("Te jössz (X)! Add meg a lépést (pl. '5 a') vagy írd: 'exit'");

            try {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) break;

                // Input feldolgozása (pl: "5 a")
                String[] parts = input.split(" ");
                int row = Integer.parseInt(parts[0]) - 1; // -1, mert a tömb 0-tól indexelődik
                int col = parts[1].charAt(0) - 'a';       // 'a' betű kódjának kivonása

                Pozicio humanPos = new Pozicio(row, col);

                // Szabályos a lépés?
                if (jatekService.isValidMove(palya, humanPos)) {
                    // Humán lépés végrehajtása
                    jatekService.makeMove(palya, humanPos, Cella.X);

                    // Nyert a humán?
                    if (jatekService.checkWin(palya, Cella.X)) {
                        System.out.println(palya);
                        System.out.println("Gratulálok, nyertél!");
                        fajlKezelo.saveGame(palya);
                        break;
                    }

                    // --- GÉPI LÉPÉS ---
                    Pozicio aiPos = jatekService.getAiMove(palya);
                    if (aiPos != null) {
                        jatekService.makeMove(palya, aiPos, Cella.O);
                        System.out.println("A gép ide lépett: " + (aiPos.row() + 1) + " " + (char)('a' + aiPos.col()));

                        // Nyert a gép?
                        if (jatekService.checkWin(palya, Cella.O)) {
                            System.out.println(palya);
                            System.out.println("A gép nyert! Próbáld újra.");
                            break;
                        }
                    } else {
                        System.out.println("Döntetlen! Betelt a tábla.");
                        break;
                    }

                    // Mentés minden kör végén
                    fajlKezelo.saveGame(palya);

                } else {
                    System.out.println("HIBA: Érvénytelen lépés! (Foglalt mező, vagy nincs szomszédja)");
                }
            } catch (Exception e) {
                System.out.println("Hibás bemenet! Helyes formátum: 'sor oszlop' (pl: 5 c)");
            }
        }
        scanner.close();
    }
}
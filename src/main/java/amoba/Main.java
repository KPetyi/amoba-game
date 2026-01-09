package amoba;

import amoba.io.Adatbazis;
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
        Adatbazis adatbazisKezelo = new Adatbazis();

        System.out.println(" AMŐBA JÁTÉK (2 Játékos mód) ");

        System.out.print("1. játékos neve (X): ");
        String jatekos1 = scanner.nextLine();
        if (jatekos1.trim().isEmpty()) jatekos1 = "Játékos 1";

        System.out.print("2. játékos nevét (O): ");
        String jatekos2 = scanner.nextLine();
        if (jatekos2.trim().isEmpty()) jatekos2 = "Játékos 2";

        System.out.println("\n DICSŐSÉGLISTA ");
        var scores = adatbazisKezelo.getHighScores();
        if (scores.isEmpty()) {
            System.out.println("(Még nincs adat)");
        } else {
            for (String score : scores) {
                System.out.println(score);
            }
        }
        System.out.println("---------------------\n");

        System.out.println("Szeretnéd betölteni a korábbi játékállást? (i/n)");
        String choice = scanner.nextLine();

        Palya palya;
        Cella aktualisJatekos;
        if (choice.equalsIgnoreCase("i")) {
            palya = fajlKezelo.loadGame();
            aktualisJatekos = kitalalKikovetkezik(palya);
        } else {
            palya = new Palya(10, 10);
            aktualisJatekos = Cella.X;
        }

        boolean running = true;
        while (running) {
            System.out.println(palya);

            String aktualisNev = (aktualisJatekos == Cella.X) ? jatekos1 : jatekos2;

            System.out.println(aktualisNev + " (" + aktualisJatekos + ") következik! Írj koordinátát (pl. 5 a) vagy exit:");

            try {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("exit")) break;

                String[] parts = input.split(" ");
                int row = Integer.parseInt(parts[0]) - 1;
                int col = parts[1].charAt(0) - 'a';

                Pozicio pos = new Pozicio(row, col);

                if (jatekService.isValidMove(palya, pos)) {
                    jatekService.makeMove(palya, pos, aktualisJatekos);

                    if (jatekService.checkWin(palya, aktualisJatekos)) {
                        System.out.println(palya);
                        System.out.println("Gratulálok " + aktualisNev + " nyertél");

                        adatbazisKezelo.addWin(aktualisNev);
                        System.out.println("Az eredményt rögzítettük");

                        adatbazisKezelo.exportToHtml();

                        break;

                    }

                    fajlKezelo.saveGame(palya);

                    aktualisJatekos = (aktualisJatekos == Cella.X) ? Cella.O : Cella.X;

                } else {
                    System.out.println("Érvénytelen lépés (foglalt vagy pályán kívül)");
                }
            } catch (Exception e) {
                System.out.println("Hibás lépj máshova");
            }
        }
        scanner.close();
    }

    private static Cella kitalalKikovetkezik(Palya palya) {
        int xCount = 0;
        int oCount = 0;
        for(int i=0; i<palya.getRows(); i++) {
            for(int j=0; j<palya.getCols(); j++) {
                Pozicio p = new Pozicio(i, j);
                if(palya.getCell(p) == Cella.X) xCount++;
                if(palya.getCell(p) == Cella.O) oCount++;
            }
        }
        return (xCount <= oCount) ? Cella.X : Cella.O;
    }
}
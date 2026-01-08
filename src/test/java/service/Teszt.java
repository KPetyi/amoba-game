package service;

import static org.junit.jupiter.api.Assertions.*;

import amoba.model.Cella;
import amoba.model.Palya;
import amoba.model.Pozicio;
import amoba.service.Jatek;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Teszt {

    private Jatek jatek;
    private Palya palya;

    // Ez minden egyes teszt előtt lefut, hogy tiszta lappal induljunk
    @BeforeEach
    void setUp() {
        jatek = new Jatek();
        palya = new Palya(10, 10);
    }

    @Test
    void testIsValidMove_UresPalya() {
        // Üres pályára bárhova lehet lépni (szabály szerint)
        Pozicio p = new Pozicio(5, 5);
        assertTrue(jatek.isValidMove(palya, p), "Üres pályára engednie kell a lépést");
    }

    @Test
    void testIsValidMove_FoglaltMezo() {
        Pozicio p = new Pozicio(5, 5);
        // Lerakunk oda egy jelet
        jatek.makeMove(palya, p, Cella.X);

        // Megpróbálunk még egyszer oda lépni
        assertFalse(jatek.isValidMove(palya, p), "Foglalt mezőre nem engedhet lépni");
    }

    @Test
    void testIsValidMove_NincsSzomszed() {
        // Lerakunk egy jelet középre
        jatek.makeMove(palya, new Pozicio(5, 5), Cella.X);

        // Próbálunk lépni a sarokba (messze van, nincs szomszédja)
        Pozicio tavoli = new Pozicio(0, 0);
        assertFalse(jatek.isValidMove(palya, tavoli), "Ha nincs szomszéd, nem engedhet lépni");
    }

    @Test
    void testIsValidMove_VanSzomszed() {
        // Lerakunk egy jelet középre
        jatek.makeMove(palya, new Pozicio(5, 5), Cella.X);

        // Próbálunk lépni mellé
        Pozicio szomszed = new Pozicio(5, 6);
        assertTrue(jatek.isValidMove(palya, szomszed), "Szomszédos mezőre engednie kell");
    }

    @Test
    void testCheckWin_Vízszintes() {
        // Csinálunk 4 db X-et egymás mellé
        for(int i = 0; i < 4; i++) {
            jatek.makeMove(palya, new Pozicio(0, i), Cella.X);
        }
        // Ellenőrizzük, hogy nyert-e
        assertTrue(jatek.checkWin(palya, Cella.X), "4 vízszintes jelre nyernie kell");
    }

    @Test
    void testAiMove_Mukodik() {
        // Kérünk egy lépést a géptől
        Pozicio aiTipp = jatek.getAiMove(palya);

        // Nem lehet null (találnia kell lépést üres pályán)
        assertNotNull(aiTipp);
        // És érvényesnek kell lennie
        assertEquals(Cella.EMPTY, palya.getCell(aiTipp));
    }
}
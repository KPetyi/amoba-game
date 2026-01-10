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

    @BeforeEach
    void setUp() {
        jatek = new Jatek();
        palya = new Palya(10, 10);
    }

    @Test
    void testIsValidMove_UresPalya() {
        Pozicio p = new Pozicio(5, 5);
        assertTrue(jatek.isValidMove(palya, p), "Üres pályára engednie kell a lépést");
    }

    @Test
    void testIsValidMove_FoglaltMezo() {
        Pozicio p = new Pozicio(5, 5);
        jatek.makeMove(palya, p, Cella.X);
        assertFalse(jatek.isValidMove(palya, p), "Foglalt mezőre nem engedhet lépni");
    }

    @Test
    void testIsValidMove_NincsSzomszed() {
        jatek.makeMove(palya, new Pozicio(5, 5), Cella.X);

        Pozicio tavoli = new Pozicio(0, 0);
        assertFalse(jatek.isValidMove(palya, tavoli), "Ha nincs szomszéd, nem engedhet lépni");
    }

    @Test
    void testIsValidMove_VanSzomszed() {
        jatek.makeMove(palya, new Pozicio(5, 5), Cella.X);

        Pozicio szomszed = new Pozicio(5, 6);
        assertTrue(jatek.isValidMove(palya, szomszed), "Szomszédos mezőre engednie kell");
    }

    @Test
    void testCheckWin_Vízszintes() {
        for(int i = 0; i < 4; i++) {
            jatek.makeMove(palya, new Pozicio(0, i), Cella.X);
        }
        assertTrue(jatek.checkWin(palya, Cella.X), "5 vízszintes jelre nyernie kell");
    }
}
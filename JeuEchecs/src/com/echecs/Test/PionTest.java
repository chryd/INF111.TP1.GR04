package com.echecs.Test;

import com.echecs.*;
import com.echecs.pieces.*;
import org.junit.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class PionTest {
    Piece pion = new Pion('n');
    Position initiale = new Position('b', (byte) 2);
    Piece[][] echiquier = new Piece[8][8];

    @Test
    public void testConstructeur(){
        assertTrue(pion instanceof Pion);
        assertEquals(pion.getCouleur(), 'n');
    }

    @Test
    public void testDeplacementSimple() {
        Position pasSimple = new Position('b', (byte) 3);
        Position invalide = new Position('e', (byte) 3);

        assertTrue(pion.peutSeDeplacer(initiale, pasSimple, echiquier));
        assertFalse(pion.peutSeDeplacer(initiale, invalide, echiquier));
    }

    @Test
    public void testPasDoubleValide(){
        Position pasDoubleValide = new Position('b', (byte) 4);
        assertTrue(pion.peutSeDeplacer(initiale, pasDoubleValide, echiquier));
    }
    @Test
    public void testAttaqueValide(){
        echiquier[2][5] = new Pion('b'); //C3
        Position attaqueValide = new Position('c', (byte) 3);
        assertTrue(pion.peutSeDeplacer(initiale, attaqueValide, echiquier));
    }

    @Test
    public void testAttaqueInalide(){
        echiquier[4][1] = new Pion('n'); //E7
        echiquier[4][7] = new Roi('b'); //E1
        Position attaqueInvalide = new Position('e', (byte) 1);
        Position pionN = new Position('e', (byte) 7);

        assertFalse(pion.peutSeDeplacer(pionN, attaqueInvalide, echiquier));
    }

    @Test
    public void testContreSens(){
        Position contresens = new Position('b', (byte) 1);
        assertFalse(pion.peutSeDeplacer(initiale, contresens, echiquier));
    }

    @Test
    public void testPasDoubleInvalide(){
        Position initiale2 = new Position('e', (byte) 3);
        Position pasDoubleInvalide = new Position('e', (byte) 5);
        assertFalse(pion.peutSeDeplacer(initiale2, pasDoubleInvalide, echiquier));
    }

    @Test
    public void testAttaqueVide(){
        Position attaqueVide = new Position('c', (byte) 3);
        assertFalse(pion.peutSeDeplacer(initiale, attaqueVide, echiquier));
    }

}

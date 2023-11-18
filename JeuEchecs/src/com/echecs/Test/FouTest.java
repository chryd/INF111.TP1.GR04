package com.echecs.Test;

import com.echecs.*;
import com.echecs.pieces.*;
import org.junit.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class FouTest {
    Piece fou = new Fou('n');

    Position initiale = new Position('f', (byte) 5); //F5
    Position diagonaleValide = new Position('d', (byte) 7); //D7
    Piece[][] echiquier = new Piece[8][8];

    @Test
    public void testConstructeur() {
        assertTrue(fou instanceof Fou);
        assertEquals(fou.getCouleur(), 'n');
    }

    @Test
    public void testPeutSeDeplacerBase() {

        assertTrue(fou.peutSeDeplacer(initiale, diagonaleValide, echiquier));
    }

    @Test
    public void testPeutSeDeplacerAttaque() {
        Piece victim = new Pion('b');
        echiquier[1][3] = victim; //at D7

        assertTrue(fou.peutSeDeplacer(initiale, diagonaleValide, echiquier));
    }

    @Test
    public void testPeutSeDeplacerInvalideBase() {
        //
        Position invalide = new Position('f', (byte) 6);
        assertFalse(fou.peutSeDeplacer(initiale, invalide, echiquier));
    }

    @Test
    public void testPeutSeDeplacerInvalideIntheWay() {
        //
        Piece inTheWay = new Pion('n');
        echiquier[4][2] = inTheWay; //at E6
        assertFalse(fou.peutSeDeplacer(initiale, diagonaleValide, echiquier));
    }
}

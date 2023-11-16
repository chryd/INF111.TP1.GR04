package com.echecs.Test;

import com.echecs.*;
import com.echecs.pieces.*;
import org.junit.*;

import static org.junit.Assert.*;

public class CavalierTest{
    Piece cavalier = new Cavalier('n');
    Position initiale = new Position('d', (byte) 4);

    Piece[][] echiquier = new Piece[8][8];
    @Test
    public void testConstructeur(){
        assertTrue(cavalier instanceof Cavalier);
        assertEquals(cavalier.getCouleur(), 'n');
    }

    @Test
    public void testPeutSeDeplacerValide() {
        Position valide1 = new Position('c', (byte) 6);
        Position valide2 = new Position('b', (byte) 5);
        Position valide3 = new Position('e', (byte) 6);
        Position valide4 = new Position('f', (byte) 5);
        Position valide5 = new Position('e', (byte) 2);
        Position valide6 = new Position('f', (byte) 3);
        Position valide7 = new Position('b', (byte) 3);
        Position valide8 = new Position('c', (byte) 2);

        Position[] valides = {valide1, valide2, valide3, valide4, valide5, valide6, valide7, valide8};
        for (Position p : valides) {
            assertTrue(cavalier.peutSeDeplacer(initiale, p, echiquier));
        }
    }
    @Test
    public void testPeutSeDeplacerInvalide() {
        Position invalide = new Position('d', (byte) 7);
        assertFalse(cavalier.peutSeDeplacer(initiale, invalide, echiquier));
    }
}

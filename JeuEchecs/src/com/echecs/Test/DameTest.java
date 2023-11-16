package com.echecs.Test;

import com.echecs.*;
import com.echecs.pieces.*;
import org.junit.*;

import static org.junit.Assert.*;

public class DameTest {
    Piece dame = new Dame('n');
    Piece[][] echiquier = new Piece[8][8];
    @Test
    public void testConstructeur(){
        assertTrue(dame instanceof Dame);
        assertEquals(dame.getCouleur(), 'n');
    }

    @Test
    public void testPeutSeDeplacer(){
        Position initiale = new Position('d', (byte) 4);
        Position diagonaleValide = new Position('a', (byte) 7);
        Position colonneValide = new Position('d', (byte) 1);
        Position ligneValide = new Position('f', (byte) 4);

        Position[] valides = {diagonaleValide, colonneValide, ligneValide};

        for (Position p : valides) {
            assertTrue(dame.peutSeDeplacer(initiale, p, echiquier));
        }

        Position invalide = new Position('e', (byte) 6);

        Piece inTheWay = new Pion('b');
        echiquier[3][2] = inTheWay; //at c 5
        echiquier[6][3] = inTheWay; //at d 3
        echiquier[4][4] = inTheWay; //at e 4

        for (Position p : valides) {
            assertFalse(dame.peutSeDeplacer(initiale, p, echiquier));
        }

        assertFalse(dame.peutSeDeplacer(initiale, invalide, echiquier));
    }

}

package com.echecs.Test;

import com.echecs.*;
import com.echecs.pieces.*;
import org.junit.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class RoiTest {
    Piece roi = new Roi('n');
    Position initiale = new Position('d', (byte) 4);
    Position diagonaleValide = new Position('c', (byte) 5);
    Position colonneValide = new Position('d', (byte) 3);
    Position ligneValide = new Position('e', (byte) 4);

    Position[] valides = {diagonaleValide, colonneValide, ligneValide};
    Piece[][] echiquier = new Piece[8][8];
    @Test
    public void testConstructeur(){
        assertTrue(roi instanceof Roi);
        assertEquals(roi.getCouleur(), 'n');
    }

    @Test
    public void testPeutSeDeplacer() {
        for (Position p : valides) {
            assertTrue(roi.peutSeDeplacer(initiale, p, echiquier));
        }
    }

    @Test
    public void testPeutSeDeplacerInvalidePosition() {
        Position invalide = new Position('e', (byte) 6);
        assertFalse(roi.peutSeDeplacer(initiale, invalide, echiquier));

        Position rb = new Position('e', (byte) 1);
        Position rn = new Position('e', (byte) 8);
        assertFalse(roi.peutSeDeplacer(rb, rn, echiquier));

    }

}

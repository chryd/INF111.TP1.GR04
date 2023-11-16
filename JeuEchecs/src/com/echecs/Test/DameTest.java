package com.echecs.Test;

import com.echecs.*;
import com.echecs.pieces.*;
import org.junit.*;

import static org.junit.Assert.*;

public class DameTest {
    Piece dame = new Dame('n');
    Position initiale = new Position('d', (byte) 4);
    Position diagonaleValide = new Position('a', (byte) 7);
    Position colonneValide = new Position('d', (byte) 1);
    Position ligneValide = new Position('f', (byte) 4);

    Position[] valides = {diagonaleValide, colonneValide, ligneValide};
    Piece[][] echiquier = new Piece[8][8];
    @Test
    public void testConstructeur(){
        assertTrue(dame instanceof Dame);
        assertEquals(dame.getCouleur(), 'n');
    }

    @Test
    public void testPeutSeDeplacer() {
        for (Position p : valides) {
            assertTrue(dame.peutSeDeplacer(initiale, p, echiquier));
        }
    }
    @Test
    public void testPeutSeDeplacerInvalidePosition() {
        Position invalide = new Position('e', (byte) 6);
        assertFalse(dame.peutSeDeplacer(initiale, invalide, echiquier));
    }

    @Test
    public void testPeutSeDeplacerInTheWay() {
        Piece inTheWay = new Pion('n');
        echiquier[3][2] = inTheWay; //at C5 en diagonale
        echiquier[3][6] = inTheWay; //at D3 en colonne
        echiquier[4][4] = inTheWay; //at E4 en ligne

        //assertFalse(dame.peutSeDeplacer(initiale, diagonaleValide, echiquier));
        assertFalse(dame.peutSeDeplacer(initiale, colonneValide, echiquier));
        assertFalse(dame.peutSeDeplacer(initiale, ligneValide, echiquier));

    }

}

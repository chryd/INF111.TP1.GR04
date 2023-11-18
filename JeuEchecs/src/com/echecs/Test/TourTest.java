package com.echecs.Test;

import com.echecs.*;
import com.echecs.pieces.*;
import org.junit.*;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class TourTest {
    Piece tour = new Tour('n');
    Position initiale = new Position('d', (byte) 4);
    Position colonneValide = new Position('d', (byte) 1);
    Position ligneValide = new Position('f', (byte) 4);

    Position[] valides = {colonneValide, ligneValide};
    Piece[][] echiquier = new Piece[8][8];
    @Test
    public void testConstructeur(){
        assertTrue(tour instanceof Tour);
        assertEquals(tour.getCouleur(), 'n');
    }

    @Test
    public void testPeutSeDeplacer() {
        for (Position p : valides) {
            assertTrue(tour.peutSeDeplacer(initiale, p, echiquier));
        }
    }
    @Test
    public void testPeutSeDeplacerInvalidePosition() {
        Position invalide = new Position('e', (byte) 6);
        assertFalse(tour.peutSeDeplacer(initiale, invalide, echiquier));
    }

    @Test
    public void testPeutSeDeplacerInTheWay() {
        Piece inTheWay = new Pion('n');
        echiquier[3][6] = inTheWay; //at D3 en colonne
        echiquier[4][4] = inTheWay; //at E4 en ligne

        //assertFalse(dame.peutSeDeplacer(initiale, diagonaleValide, echiquier));
        assertFalse(tour.peutSeDeplacer(initiale, colonneValide, echiquier));
        assertFalse(tour.peutSeDeplacer(initiale, ligneValide, echiquier));

    }
}

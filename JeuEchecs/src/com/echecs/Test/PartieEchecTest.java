package com.echecs.Test;

import com.echecs.*;
import org.junit.*;

import static org.junit.Assert.*;

public class PartieEchecTest {
    PartieEchecs partie = new PartieEchecs();

    /*@Test
    public void testPartieEchecsConstructor() {

        Field field = PartieEchecs.class.getField(echiquier);

        // Test the initialization of the chessboard
        assertNotNull(partie.echiquier);
        assertEquals(8, partie.echiquier.length);
        assertEquals(8, partie.echiquier[0].length);

        // Add more specific tests based on your constructor logic
        // For example, test the placement of specific pieces

        // Test the initialization of player colors
        assertTrue(partie.couleurJoueur1 == 'b' || partie.couleurJoueur1 == 'n');
        assertTrue(partie.couleurJoueur2 == 'b' || partie.couleurJoueur2 == 'n');
        assertNotEquals(partie.couleurJoueur1, partie.couleurJoueur2);

        // Test the initialization of other attributes
        assertEquals('b', partie.tour);
        assertFalse(partie.roiBouge);
        assertFalse(partie.tour1Bougee);
        assertFalse(partie.tour2Bougee);
    }*/
    @Test
    public void testDeplacementPiece() {
        PartieEchecs partie = new PartieEchecs();

        // Assuming the initial positions are valid for testing
        Position initiale = new Position('a', (byte) 2);
        Position finale = new Position('a', (byte) 4);

        assertTrue(partie.deplace(initiale, finale));

        // Add more test cases as needed
    }

    @Test
    public void testEstEnEchec() {
        PartieEchecs partie = new PartieEchecs();

        // Assuming the initial positions are valid for testing
        // Move some pieces to create a specific board situation

        // Test if a specific color king is in check
        assertEquals('x', partie.estEnEchec());

        // Add more test cases as needed
    }

}

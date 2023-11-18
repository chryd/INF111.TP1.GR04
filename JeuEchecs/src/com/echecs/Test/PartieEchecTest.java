package com.echecs.Test;

import com.echecs.*;
import com.echecs.pieces.*;
import org.junit.*;

import static org.junit.Assert.*;

public class PartieEchecTest {

    PartieEchecs partie = new PartieEchecs();

    @Test
    public void testConstructeur(){
        Piece[][] expected = new Piece[8][8];
        Piece[][] echiquier = partie.getEchiquier();
        //Placement des pièces :

        //placement des pions
        for(int i = 0; i < 8; i++){
            expected[i][1] = new Pion('n');
            expected[i][6] = new Pion('b');
        }

        //placement des tours
        expected[0][0] = new Tour('n');
        expected[7][0] = new Tour('n');
        expected[0][7] = new Tour('b');
        expected[7][7] = new Tour('b');

        //placement des cavaliers
        expected[1][0] = new Cavalier('n');
        expected[6][0] = new Cavalier('n');
        expected[1][7] = new Cavalier('b');
        expected[6][7] = new Cavalier('b');

        //placement des fous
        expected[2][0] = new Fou('n');
        expected[5][2] = new Fou('n');
        expected[2][7] = new Fou('b');
        expected[5][7] = new Fou('b');

        //placement des dames
        expected[3][0] = new Dame('n');
        expected[3][7] = new Dame('b');

        //placement des rois
        expected[4][0] = new Roi('n');
        expected[4][7] = new Roi('b');

        for (int i=0; i < 8; i++){
            for (int j=0; j < 8; j++){
                try {
                    Piece pExpected = expected[i][j];
                    Piece pPlanche = echiquier[i][j];
                    assertEquals(pExpected.getClass(), pPlanche.getClass());
                } catch (NullPointerException e){
                    continue;
                }
            }
        }

        assertEquals(partie.getTour(), 'b');
    }

    @Test
    public void testNEstPasEnEchec(){
        assertEquals(partie.estEnEchec(),'x');
    }

    /*@Test
    public void testTrouveRoi(){
        Position rb = new Position('e', (byte) 1);
        Position rn = new Position('e', (byte) 8);
        assertEquals(partie.trouveRoi('b'), rb);
        assertEquals(partie.trouveRoi('n'), rn);
    }*/

    /*@Test
    public void testVerfieRoi(){
        Position rb = new Position('e', (byte) 1);
        Position rn = new Position('e', (byte) 8);
        assertFalse(partie.verifieEchec('b', rb));
        assertFalse(partie.verifieEchec('n', rn));
    }*/

    @Test
    public void testDeplacementPionSimpleBlanc(){
        PartieEchecs nouvellePartie = new PartieEchecs();

        Position pb1 = new Position('e', (byte) 2);
        Position pb2 = new Position('e', (byte) 4);

        System.out.println(nouvellePartie.getEchiquier()[4][6]);
        System.out.println(nouvellePartie.getEchiquier()[4][4]);
        boolean d1 = nouvellePartie.deplace(pb1, pb2);

        assertTrue(d1);
    }

    @Test
    public void testDeplacementPionSimpleNoir(){
        PartieEchecs nouvellePartie = new PartieEchecs();

        Position pb1 = new Position('e', (byte) 2);
        Position pb2 = new Position('e', (byte) 4);

        Position pn1 = new Position('f', (byte) 7);
        Position pn2 = new Position('f', (byte) 5);
        boolean d1 = nouvellePartie.deplace(pb1, pb2);

        System.out.println(nouvellePartie.getEchiquier()[5][1]);
        System.out.println(nouvellePartie.getEchiquier()[5][2]);
        System.out.println(nouvellePartie.getEchiquier()[5][3]);
        boolean d2 = nouvellePartie.deplace(pn1, pn2);

        assertTrue(d2);
    }

    @Test
    public void testAttaquePion(){
        PartieEchecs nouvellePartie = new PartieEchecs();

        Position pb1 = new Position('e', (byte) 2);
        Position pb2 = new Position('e', (byte) 4);

        Position pn1 = new Position('f', (byte) 7);
        Position pn2 = new Position('f', (byte) 5);
        boolean d1 = nouvellePartie.deplace(pb1, pb2);
        boolean d2 = nouvellePartie.deplace(pn1, pn2);


        System.out.println(nouvellePartie.getEchiquier()[4][4]);
        System.out.println(nouvellePartie.getEchiquier()[5][3]);
        boolean d3 = nouvellePartie.deplace(pb2, pn2);
        System.out.println(nouvellePartie.getEchiquier()[5][3]);

        assertTrue(d3);
    }

    @Test
    public void testDameDiagonale(){
        PartieEchecs nouvellePartie = new PartieEchecs();

        Position pb1 = new Position('e', (byte) 2);
        Position pb2 = new Position('e', (byte) 4);

        Position pn1 = new Position('f', (byte) 7);
        Position pn2 = new Position('f', (byte) 5);

        Position pn3 = new Position('g', (byte) 7);
        Position pn4 = new Position('g', (byte) 5);

        Position db1 = new Position('d', (byte) 1);
        Position db2 = new Position('h', (byte) 5);

        boolean d1 = nouvellePartie.deplace(pb1, pb2);
        boolean d2 = nouvellePartie.deplace(pn1, pn2);
        boolean d3 = nouvellePartie.deplace(pb2, pn2);
        boolean d4 = nouvellePartie.deplace(pn3, pn4);
        boolean d5 = nouvellePartie.deplace(db1, db2);

        assertTrue(d5);
    }

    @Test
    public void testEstEnEchec(){
        PartieEchecs nouvellePartie = new PartieEchecs();

        Position pb1 = new Position('e', (byte) 2);
        Position pb2 = new Position('e', (byte) 4);

        Position pn1 = new Position('f', (byte) 7);
        Position pn2 = new Position('f', (byte) 5);

        Position pn3 = new Position('g', (byte) 7);
        Position pn4 = new Position('g', (byte) 5);

        Position rb1 = new Position('d', (byte) 1);
        Position rb2 = new Position('h', (byte) 5);


        boolean d1 = nouvellePartie.deplace(pb1, pb2);
        boolean d2 = nouvellePartie.deplace(pn1, pn2);
        boolean d3 = nouvellePartie.deplace(pb2, pn2);
        boolean d4 = nouvellePartie.deplace(pn3, pn4);
        boolean d5 = nouvellePartie.deplace(rb1, rb2);
        assertEquals(nouvellePartie.estEnEchec(), 'n');

    }
}

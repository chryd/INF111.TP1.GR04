package com.echecs.Test;

import com.echecs.*;
import com.echecs.pieces.*;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CavalierTest{
    Piece cavalier = new Cavalier('n');
    Piece[][] echiquier = new Piece[8][8];
    @Test
    public void testConstructeur(){
        assertTrue(cavalier instanceof Cavalier);
        assertEquals(cavalier.getCouleur(), 'n');
    }

    @Test
    public void testPeutSeDeplacer(){
        Position initiale = new Position('d', (byte) 5);
        Position valide1 = new Position('c', (byte) 7);
        Position valide2 = new Position('b', (byte) 6);

        Position invalide = new Position('d', (byte) 7);
        assertTrue(cavalier.peutSeDeplacer(initiale, ));
    }
}

package com.echecs.pieces;

import com.echecs.*;

public class Roi extends Piece{
    public Roi(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {
        return pos2.estVoisineDe(pos1);
    }
}

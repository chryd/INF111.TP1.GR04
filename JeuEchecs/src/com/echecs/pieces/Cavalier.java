package com.echecs.pieces;

import com.echecs.*;

public class Cavalier extends Piece{
    public Cavalier(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {

        int x1 = pos1.getColonne()-97;
        int y1 = -(pos1.getLigne() - 8);

        int x2 = pos2.getColonne()-97;
        int y2 = -(pos2.getLigne() - 8);

        if ( x2 == x1 + 2 || x2 == x1 - 2 ){
            return y2 == y1 + 1 || y2 == y1 - 1;
        }

        if ( x2 == x1 + 1 || x2 == x1 - 1 ){
            return y2 == y1 + 2 || y2 == y1 - 2;
        }

        return false;
    }
}

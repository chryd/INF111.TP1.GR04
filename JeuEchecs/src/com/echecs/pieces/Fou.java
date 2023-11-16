package com.echecs.pieces;

import com.echecs.*;

public class Fou extends Piece{
    public Fou(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {

        int x1 = pos1.getColonne()-97;
        int y1 = -(pos1.getLigne() - 8);
        int x2 = pos2.getColonne()-97;
        int y2 = -(pos2.getLigne() - 8);

        int startX = Math.min(x1, x2);
        int endX = Math.max(x1, x2);

        int startY = Math.min(y1, y2);
        int endY = Math.max(y1, y2);

        if (pos2.estSurLaMemeDiagonaleQue(pos1)){

            for (int i = startX; i < endX; i++) {
                for (int j = startY; j < endY; j++) {
                    if (!echiquierEstVideA(i, j, echiquier)) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}

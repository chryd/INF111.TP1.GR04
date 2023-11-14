package com.echecs.Test;

import com.echecs.*;
import com.echecs.pieces.*;

public class RoiTest extends Piece {
    public RoiTest(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {
        return pos2.estVoisineDe(pos1);
    }
}

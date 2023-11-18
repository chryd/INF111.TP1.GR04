package com.echecs.pieces;

import com.echecs.*;

public class Roi extends Piece{
    public Roi(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position positionInitiale, Position positionFinale, Piece[][] echiquier) {
        if (estLaMemeCouleur(positionInitiale, positionFinale, echiquier)){
            return false;
        }
        return positionFinale.estVoisineDe(positionInitiale);
    }
}

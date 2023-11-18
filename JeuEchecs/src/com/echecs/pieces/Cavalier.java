package com.echecs.pieces;

import com.echecs.*;

public class Cavalier extends Piece{
    public Cavalier(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position positionInitiale, Position positionFinale, Piece[][] echiquier) {

        //passer de la notation classique d'un echiquier aux valeurs dans la matrice
        int colonneInitiale = positionInitiale.getColonne()-97;
        int ligneInitiale = -(positionInitiale.getLigne() - 8);

        int colonneFinale = positionFinale.getColonne()-97;
        int ligneFinale = -(positionFinale.getLigne() - 8);

        boolean output;

        if (estLaMemeCouleur(positionInitiale, positionFinale, echiquier)){
            return false;
        }

        if (colonneFinale == colonneInitiale + 2 || colonneFinale == colonneInitiale - 2) {
            output = ligneFinale == ligneInitiale + 1 || ligneFinale == ligneInitiale - 1;
        } else if (colonneFinale == colonneInitiale + 1 || colonneFinale == colonneInitiale - 1) {
            output = ligneFinale == ligneInitiale + 2 || ligneFinale == ligneInitiale - 2;
        } else {
            output = false;
        }

        return output;
    }
}

package com.echecs.pieces;

import com.echecs.*;

public class Fou extends Piece{
    public Fou(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position positionInitiale, Position positionFinale, Piece[][] echiquier) {

        //passer de la notation classique d'un echiquier aux valeurs dans la matrice
        int colonneInitiale = positionInitiale.getColonne()-97;
        int ligneInitiale = -(positionInitiale.getLigne() - 8);
        int colonneFinale = positionFinale.getColonne()-97;
        int ligneFinale = -(positionFinale.getLigne() - 8);

        //determiner les coordonnes limite de l'iteration des case
        int deltaLigne = Integer.compare(colonneFinale, colonneInitiale);
        int deltaColonne = Integer.compare(ligneFinale, ligneInitiale);
        colonneInitiale += deltaLigne;
        ligneInitiale += deltaColonne;

        //valeur a retourner
        boolean output;

        if (estLaMemeCouleur(positionInitiale, positionFinale, echiquier)){
            output = false;
        } else {
            //Si la position 2 n'est pas sur la meme diagonale que la position 1
            if (!positionFinale.estSurLaMemeDiagonaleQue(positionInitiale)) {
                return false;
            }

            while (colonneInitiale != colonneFinale || ligneInitiale != ligneFinale){
                if (!echiquierEstVideA(colonneInitiale, ligneInitiale,echiquier)){
                    return false;
                }
                colonneInitiale += deltaLigne;
                ligneInitiale += deltaColonne;

            }
            //si le chemin est libre, le deplacement est valide
            output = true;
        }

        return output;
    }
}

package com.echecs.pieces;

import com.echecs.*;

public class Dame extends Piece {
    public Dame(char couleur) {
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
        int colonneStart = Math.min(colonneInitiale, colonneFinale);
        int colonneEnd = Math.max(colonneInitiale, colonneFinale);

        int ligneStart = Math.min(ligneInitiale, ligneFinale);
        int ligneEnd = Math.max(ligneInitiale, ligneFinale);

        //determiner les coordonnes limite de l'iteration des case
        int deltaLigne = Integer.compare(colonneFinale, colonneInitiale);
        int deltaColonne = Integer.compare(ligneFinale, ligneInitiale);
        colonneInitiale += deltaLigne;
        ligneInitiale += deltaColonne;

        //valeur a retourner
        boolean output;

        if (estLaMemeCouleur(positionInitiale, positionFinale, echiquier)){
            System.out.println("mm couleur");
            return false;
        }

        //Si la position 2 est sur le meme diagonale que la position 1
        if (positionFinale.estSurLaMemeDiagonaleQue(positionInitiale)) {

            while (colonneInitiale != colonneFinale || ligneInitiale != ligneFinale){
                if (!echiquierEstVideA(colonneInitiale, ligneInitiale,echiquier)){
                    return false;
                }
                colonneInitiale += deltaLigne;
                ligneInitiale += deltaColonne;

            }
            //si le chemin est libre, le deplacement est valide
            output = true;

            //Si la position 2 est sur la meme colonne que la position 1
        } else if (positionFinale.estSurLaMemeColonneQue(positionInitiale)) {
            //verifier pour chaque case (autre qu'initial ou final) qu'elle est vide
            for (int i = ligneStart + 1; i < ligneEnd; i++) {
                if (!echiquierEstVideA(colonneInitiale, i, echiquier)) {
                    return false;
                }
            }
            output = true;
            //Si la position 2 est sur la meme ligne que la position 1
        } else if (positionFinale.estSurLaMemeLigneQue(positionInitiale)) {
            //verifier pour chaque case (autre qu'initial ou final) qu'elle est vide
            for (int i = colonneStart + 1; i < colonneEnd; i++) {
                if (!echiquierEstVideA(i, ligneInitiale, echiquier)) {
                    return false;
                }
            }
            output = true;
        } else {
            output = false;
        }

        return output;
    }
}

package com.echecs.pieces;

import com.echecs.*;
import com.echecs.util.*;

public class Dame extends Piece {
    public Dame(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position positionInitiale, Position positionFinale, Piece[][] echiquier) {

        //S'il y a une piece de la meme couleur sur la position finale
        if (estLaMemeCouleur(positionInitiale, positionFinale, echiquier)){return false;}

        //valeur a retourner
        boolean output;

        //passer de la notation classique d'un echiquier aux valeurs dans la matrice
        int colonneInitiale = EchecsUtil.indiceColonne(positionInitiale);
        int ligneInitiale = EchecsUtil.indiceLigne(positionInitiale);
        int colonneFinale = EchecsUtil.indiceColonne(positionFinale);
        int ligneFinale = EchecsUtil.indiceLigne(positionFinale);

        //Si la position 2 est sur le meme diagonale que la position 1
        if (positionFinale.estSurLaMemeDiagonaleQue(positionInitiale)) {

            //determiner les coordonnes limite de l'iteration des case en diagonal
            int deltaLigne = Integer.compare(colonneFinale, colonneInitiale);
            int deltaColonne = Integer.compare(ligneFinale, ligneInitiale);
            colonneInitiale += deltaLigne;
            ligneInitiale += deltaColonne;

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
            //determiner les coordonnes limite de l'iteration des cases en colonne
            int ligneStart = Math.min(ligneInitiale, ligneFinale);
            int ligneEnd = Math.max(ligneInitiale, ligneFinale);

            //verifier pour chaque case (autre qu'initial ou final) qu'elle est vide
            for (int i = ligneStart + 1; i < ligneEnd; i++) {
                if (!echiquierEstVideA(colonneInitiale, i, echiquier)) {
                    return false;
                }
            }

            output = true;

            //Si la position 2 est sur la meme ligne que la position 1
        } else if (positionFinale.estSurLaMemeLigneQue(positionInitiale)) {
            //determiner les coordonnes limite de l'iteration des cases en ligne
            int colonneStart = Math.min(colonneInitiale, colonneFinale);
            int colonneEnd = Math.max(colonneInitiale, colonneFinale);

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

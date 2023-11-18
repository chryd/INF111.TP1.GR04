package com.echecs.pieces;

import com.echecs.*;
import com.echecs.util.*;

public class Fou extends Piece{
    public Fou(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position positionInitiale, Position positionFinale, Piece[][] echiquier) {

        //S'il y a une piece de la meme couleur sur la position finale
        if (estLaMemeCouleur(positionInitiale, positionFinale, echiquier)){
            return false;
        }

        //passer de la notation classique d'un echiquier aux valeurs dans la matrice
        int colonneInitiale = EchecsUtil.indiceColonne(positionInitiale);
        int ligneInitiale = EchecsUtil.indiceLigne(positionInitiale);
        int colonneFinale = EchecsUtil.indiceColonne(positionFinale);
        int ligneFinale = EchecsUtil.indiceLigne(positionFinale);

        //determiner les coordonnes limite de l'iteration des case
        int deltaLigne = Integer.compare(colonneFinale, colonneInitiale);
        int deltaColonne = Integer.compare(ligneFinale, ligneInitiale);
        colonneInitiale += deltaLigne;
        ligneInitiale += deltaColonne;


        //Si la position 2 n'est pas sur la meme diagonale que la position 1
        if (!positionFinale.estSurLaMemeDiagonaleQue(positionInitiale)) {
            return false;
        }

        //verifier que le chemin est vide
        while (colonneInitiale != colonneFinale || ligneInitiale != ligneFinale){
            //sinon, le deplacement n'est pas possible
            if (!echiquierEstVideA(colonneInitiale, ligneInitiale,echiquier)){
                return false;
            }
            colonneInitiale += deltaLigne;
            ligneInitiale += deltaColonne;

        }

        return true;
    }
}

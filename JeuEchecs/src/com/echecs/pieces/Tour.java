package com.echecs.pieces;

import com.echecs.*;
import com.echecs.util.*;

public class Tour extends Piece{
    public Tour(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position positionInitiale, Position positionFinale, Piece[][] echiquier) {

        //passer de la notation classique d'un echiquier aux valeurs dans la matrice
        int colonneInitiale = EchecsUtil.indiceColonne(positionInitiale);
        int ligneInitiale = EchecsUtil.indiceLigne(positionInitiale);
        int colonneFinale = EchecsUtil.indiceColonne(positionFinale);
        int ligneFinale = EchecsUtil.indiceLigne(positionFinale);

        //determiner les coordonnes limite de l'iteration des case
        int colonneStart = Math.min(colonneInitiale, colonneFinale);
        int colonneEnd = Math.max(colonneInitiale, colonneFinale);
        int ligneStart = Math.min(ligneInitiale, ligneFinale);
        int ligneEnd = Math.max(ligneInitiale, ligneFinale);

        //valeur à retourner
        boolean ouptput;

        //Si la position 2 est sur la meme colonne que la position 1
        if (positionFinale.estSurLaMemeColonneQue(positionInitiale)) {
            //verifier pour chaque case (autre qu'initial ou final) qu'elle est vide
            for (int i = ligneStart + 1; i < ligneEnd; i++) {
                if (!echiquierEstVideA(colonneInitiale, i, echiquier)) {
                    return false;
                }
            }
            ouptput = true;
            //Si la position 2 est sur la meme ligne que la position 1
        } else if (positionFinale.estSurLaMemeLigneQue(positionInitiale)) {
            //verifier pour chaque case (autre qu'initial ou final) qu'elle est vide
            for (int i = colonneStart + 1; i < colonneEnd; i++) {
                if (!echiquierEstVideA(i, ligneInitiale, echiquier)) {
                    return false;
                }
            }
            ouptput =  true;
        } else {
            ouptput =  false;
        }
        return  ouptput;
    }
}

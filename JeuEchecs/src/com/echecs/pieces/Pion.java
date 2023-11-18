package com.echecs.pieces;

import com.echecs.*;

public class Pion extends Piece{
    public Pion(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position positionInitiale, Position positionFinale, Piece[][] echiquier) {

        boolean output = false;

        int colonneInitiale = positionInitiale.getColonne()-97;
        int ligneInitiale = -positionInitiale.getLigne() + 8;
        int colonneFinale = positionFinale.getColonne()-97;
        int ligneFinale = -(positionFinale.getLigne() - 8);

        if (estLaMemeCouleur(positionInitiale, positionFinale, echiquier)){
            return false;
        }

        //deplacements acceptes
        int deplacement = 1;
        int grandDeplacement = 0;

        //Cas ou le pion est encore a sa position initiale
        if (ligneInitiale == 1 || ligneInitiale == 6){
            grandDeplacement = 2;
        }

        //Si la couleur est blanche, le sens est inverse
        if (couleur == 'b') {
            deplacement = deplacement * -1;
            grandDeplacement = grandDeplacement * -1;
        }

        //Dans le cas d'un déplacement passif
        if (positionFinale.estSurLaMemeColonneQue(positionInitiale)){

            //s'assurer qu'il n'y a aucune piece
            if (!echiquierEstVideA(colonneFinale, ligneFinale, echiquier)){

                return false;
            }

            //Si on veut effectuer un mouvement simple
            if (ligneFinale == (ligneInitiale + deplacement)) {

                output = true;

            } else if (ligneFinale == (ligneInitiale + grandDeplacement)) {

                //s'assurer que le premier pas peut se faire (la case est vide)
                if (!echiquierEstVideA(colonneInitiale, ligneInitiale + deplacement, echiquier)){
                    return false;
                }

                output = true;

            } else {

                output = false;
            }

            //Dans le cas d'une attaque
            //s'assurer que le movement se fait en diagonal et seulement dans la direction possible
        } else if (positionFinale.estSurLaMemeDiagonaleQue(positionInitiale) && ligneFinale == ligneInitiale + deplacement) {

            //s'assurer qu'il y a une pièce a captuer à la position finale
            if (echiquierEstVideA(colonneFinale, ligneFinale, echiquier)){
                return false;
            }

            output = true;

        } else {

            output = false;
        }

        return output;
    }
}

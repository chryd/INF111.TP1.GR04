package com.echecs.pieces;

import com.echecs.*;

public class Pion extends Piece{
    public Pion(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {

        int x1 = pos1.getColonne()-97;
        int y1 = -(pos1.getLigne() - 8);
        int x2 = pos2.getColonne()-97;
        int y2 = -(pos2.getLigne() - 8);

        //deplacement accepter en y
        int movY = 1;
        int specialMovY = 0;

        //Cas ou le pion est encore a sa position initiale
        if (y1 == 1 || y1 == 6){
            specialMovY = 2;
        }

        //Si la couleur est blanche, le sens est inverse
        if (couleur == 'b') {
            movY = -1;
            specialMovY = -2;
        }

        //Dans le cas d'un déplacement passif
        if (pos2.estSurLaMemeColonneQue(pos1)){
            //Si on veut effectuer un mouvement simple

            if (y2 == (y1 + movY)) {
                //s'assurer qu'il n'y a aucune piece
                return echiquierEstVideA(x2, y2, echiquier);
            }

            //si on veut faire 2 pas
            if (y2 == (y1 + specialMovY)) {

                //s'assurer que le premier pas peut se faire (la case est vide)
                if (echiquierEstVideA(x1, y1 + movY, echiquier)){
                    return false;
                }

                //s'assurer qu'il n'y a pas de pieces
                return echiquierEstVideA(x2, y2, echiquier);
            }

            //Dans le cas d'une attaque
        } else {
            //s'assurer que le movement se fait en diagonal et seulement dans la position possible
            if (pos2.estSurLaMemeDiagonaleQue(pos1) && y2 == y1 + movY) {
                //s'assurer qu'il y a une pièce à la position finale
                return !echiquierEstVideA(x2, y2, echiquier);
            }
        }
        return false;
    }
}

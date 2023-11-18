package echecs.pieces;

import com.echecs.Position;

public class Pion extends Piece {
    public Pion(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {
        boolean output = false;

        int colonneInitiale = pos1.getColonne()-97;
        int ligneInitiale = -(pos1.getLigne() - 8);
        int colonneFinale = pos2.getColonne()-97;
        int ligneFinale = -(pos2.getLigne() - 8);

        //deplacement accepter en y
        int deplacement = 1;
        int grandDeplacement = 0;

        //Cas ou le pion est encore a sa position initiale
        if (ligneInitiale == 1 || ligneInitiale == 6){
            grandDeplacement = 2;
        }

        //Si la couleur est blanche, le sens est inverse
        if (couleur == 'n') {
            deplacement = deplacement * -1;
            grandDeplacement = grandDeplacement * -1;
        }

        //Dans le cas d'un déplacement passif
        if (pos2.estSurLaMemeColonneQue(pos1)){

            //Si on veut effectuer un mouvement simple
            if (ligneFinale == (ligneInitiale + deplacement)) {
                //s'assurer qu'il n'y a aucune piece
                output = echiquierEstVideA(colonneFinale, ligneFinale, echiquier);

                //si on veut faire 2 pas
            } else if (ligneFinale == (ligneInitiale + grandDeplacement)) {
                //s'assurer que le premier pas peut se faire (la case est vide)
                if (!echiquierEstVideA(colonneInitiale, ligneInitiale + deplacement, echiquier)){
                    return false;
                }
                //s'assurer qu'il n'y a pas de pieces
                output = echiquierEstVideA(colonneFinale, ligneFinale, echiquier);
            }

            //Dans le cas d'une attaque
        } else if (pos2.estSurLaMemeDiagonaleQue(pos1) && ligneFinale == ligneInitiale + deplacement) {
            //s'assurer que le movement se fait en diagonal et seulement dans la position possible
            //s'assurer qu'il y a une pièce à la position finale
            output = !echiquierEstVideA(colonneFinale, ligneFinale, echiquier);
        } else {
            output = false;
        }
        return output;
    }
}

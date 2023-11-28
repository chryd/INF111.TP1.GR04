package echecs.pieces;

import echecs.*;
import echecs.util.*;

public class Cavalier extends Piece {
    public Cavalier(char couleur) {
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

        boolean output;

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

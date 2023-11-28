package echecs.pieces;

import echecs.*;

public class Roi extends Piece {
    public Roi(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position positionInitiale, Position positionFinale, Piece[][] echiquier) {

        //S'il y a une piece de la meme couleur sur la position finale
        if (estLaMemeCouleur(positionInitiale, positionFinale, echiquier)){
            return false;
        }
        return positionFinale.estVoisineDe(positionInitiale);
    }
}

package echecs.pieces;

import com.echecs.Position;

public class Dame extends Piece {
    public Dame(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {

        //passer de la notation classique d'un echiquier aux valeurs dans la matrice
        int colonneInitiale = pos1.getColonne()-97;
        int ligneInitiale = -(pos1.getLigne() - 8);
        int colonneFinale = pos2.getColonne()-97;
        int ligneFinale = -(pos2.getLigne() - 8);

        //determiner les coordonnes limite de l'iteration des case
        int colonneStart = Math.min(colonneInitiale, colonneFinale);
        int colonneEnd = Math.max(colonneInitiale, colonneFinale);

        int ligneStart = Math.min(ligneInitiale, ligneFinale);
        int ligneEnd = Math.max(ligneInitiale, ligneFinale);

        //valeur a retourner
        boolean output;

        //Si la position 2 est sur le meme diagonale que la position 1
        if (pos2.estSurLaMemeDiagonaleQue(pos1)) {
            //verifier pour chaque case (autre qu'initial ou final) qu'elle est vide
            for (int i = colonneStart; i < colonneEnd; i++) {
                for (int j = ligneStart; j < ligneEnd; j++) {
                    if (!echiquierEstVideA(i, j, echiquier)) {
                        return false;
                    }
                }
            }
            //si le chemin est libre, le deplacement est valide
            output = true;

            //Si la position 2 est sur la meme colonne que la position 1
        } else if (pos2.estSurLaMemeColonneQue(pos1)) {
            //verifier pour chaque case (autre qu'initial ou final) qu'elle est vide
            for (int i = ligneStart + 1; i < ligneEnd; i++) {
                if (!echiquierEstVideA(colonneInitiale, i, echiquier)) {
                    return false;
                }
            }
            output = true;
            //Si la position 2 est sur la meme ligne que la position 1
        } else if (pos2.estSurLaMemeLigneQue(pos1)) {
            //verifier pour chaque case (autre qu'initial ou final) qu'elle est vide
            for (int i = colonneStart + 1; i < colonneEnd; i++) {
                if (!echiquierEstVideA(i, ligneInitiale, echiquier)) {
                    return false;
                }
            }
            output =  true;
        } else {
            output = false;
        }
        return output;
    }
}

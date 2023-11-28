package com.echecs.pieces;

import com.echecs.Position;
/**
 * Représente une pièce dans un jeu d'échecs. Cette classe abstraite constitue
 * la base pour les classes de pièces concrètes.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public abstract class Piece {
    protected char couleur; //b ou n
    public Piece(char couleur) {
        this.couleur = couleur;
    }

    public char getCouleur() {
        return couleur;
    }

    /**
     * Vérifie si la piece peut se déplacer d'une position à une autre.
     *
     * @param pos1 Position La position initiale
     * @param pos2 Position La position finale
     * @param echiquier Piece[][] Échiquier contenant les pièces d'une partie d'échecs
     * @return boolean true, si la pièce peut se déplacer de la position pos1 à la position pos2, false sinon
     */
    public abstract boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier);

    /**
     *
     *
     * @param ligne
     * @param colonne
     * @return
     */
    protected static boolean echiquierEstVideA(int colonne, int ligne, Piece[][] echiquier){
        return echiquier[colonne][ligne] == null;
    }

    protected boolean estLaMemeCouleur(Position positionInitiale, Position positionFinale, Piece[][] echiquier){
        //passer de la notation classique d'un echiquier aux valeurs dans la matrice
        int colonneInitiale = positionInitiale.getColonne()-97;
        int ligneInitiale = -(positionInitiale.getLigne() - 8);

        int colonneFinale = positionFinale.getColonne()-97;
        int ligneFinale = -(positionFinale.getLigne() - 8);

        Piece pieceInitiale = echiquier[colonneInitiale][ligneInitiale];
        Piece pieceFinale = echiquier[colonneFinale][ligneFinale];

        if(echiquierEstVideA(colonneFinale, ligneFinale, echiquier)) {
            return false;
        }

        return pieceFinale.getCouleur() == pieceInitiale.getCouleur();
    }

    @Override
    public String toString() {
        return String.valueOf(this.getClass()) + this.couleur;
    }
}

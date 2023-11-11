package com.echecs;

import com.echecs.pieces.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Représente une partie de jeu d'échecs. Orcheste le déroulement d'une partie :
 * déplacement des pièces, vérification d'échec, d'échec et mat,...
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class PartieEchecs {
    /**
     * Grille du jeu d'échecs. La ligne 0 de la grille correspond à la ligne
     * 8 de l'échiquier. La colonne 0 de la grille correspond à la colonne a
     * de l'échiquier.
     */
    private Piece[][] echiquier;
    private String aliasJoueur1, aliasJoueur2;
    private char couleurJoueur1, couleurJoueur2;

    /**
     * Si le roi a deja ete bouge
     */
    private boolean roiBouge;

    /**
     * Si chaques tours ont deja ete bougees
     */
    private boolean tour1Bougee;
    private boolean tour2Bougee;

    /**
     * La couleur de celui à qui c'est le tour de jouer (n ou b).
     */
    private char tour; //Les blancs commencent toujours
    /**
     * Crée un échiquier de jeu d'échecs avec les pièces dans leurs positions
     * initiales de début de partie.
     * Répartit au hasard les couleurs n et b entre les 2 joueurs.
     */
    public PartieEchecs() {
        echiquier = new Piece[8][8];
        //Placement des pièces :

        //placement des pions
        for(int i = 0; i < 8; i++){
            echiquier[1][i] = new Pion('n');
            echiquier[6][i] = new Pion('b');
        }

        //placement des tours
        echiquier[0][0] = new Tour('n');
        echiquier[0][7] = new Tour('n');
        echiquier[7][0] = new Tour('b');
        echiquier[7][7] = new Tour('b');

        //placement des cavaliers
        echiquier[0][1] = new Cavalier('n');
        echiquier[0][6] = new Cavalier('n');
        echiquier[7][1] = new Cavalier('b');
        echiquier[7][6] = new Cavalier('b');

        //placement des fous
        echiquier[0][2] = new Fou('n');
        echiquier[0][5] = new Fou('n');
        echiquier[7][2] = new Fou('b');
        echiquier[7][5] = new Fou('b');

        //placement des dames
        echiquier[0][3] = new Dame('n');
        echiquier[7][3] = new Dame('b');

        //placement des rois
        echiquier[0][4] = new Roi('n');
        echiquier[7][4] = new Roi('b');

        Random rand = new Random();
        if (rand.nextInt(2) == 0) {
            couleurJoueur1 = 'b';
            couleurJoueur2 = 'n';
        } else {
            couleurJoueur1 = 'n';
            couleurJoueur2 = 'b';
        }
        tour = 'b';
        roiBouge = false;
        tour1Bougee = false;
        tour2Bougee = false;

    }

    /**
     * Change la main du jeu (de n à b ou de b à n).
     */
    public void changerTour() {
        if (tour=='b')
            tour = 'n';
        else
            tour = 'b';
    }

    /**
     *
     * @param position
     * @return
     */
    private boolean positionInvalides(Position position){
        return position.getLigne() > 8 || position.getLigne() < 1 || position.getColonne() > 'h' || position.getColonne() < 'a';
    }

    /**
     *
     * @param ligneInitiale
     * @param debut
     * @param fin
     * @return
     */
    private boolean cheminEstVide(int ligneInitiale, int debut, int fin){
        for (int i = debut; i < fin; i++) {
            if (echiquier[ligneInitiale][i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param colonneInitial
     * @param colonneFinale
     * @param ligne
     * @return
     */
    private boolean mouvementRoqueValide(int colonneInitial, int colonneFinale, int ligne){
        if (colonneFinale < colonneInitial) {
            return !tour1Bougee && cheminEstVide(ligne, 1, 4);
        } else {
            return !tour2Bougee && cheminEstVide(ligne, 5, 8);
        }
    }

    /**
     *
     * @param colonneInitiale
     * @param colonneFinale
     * @param ligneInitiale
     * @param ligneFinale
     * @return
     */
    private boolean deplaceRoque(int colonneInitiale,  int colonneFinale, int ligneInitiale, int ligneFinale){
        //si le roi ou la tour ont deja bouges, roque invalide
        if (!roiBouge && mouvementRoqueValide(colonneInitiale, colonneFinale, ligneInitiale)){
            echiquier[ligneFinale][colonneFinale] = echiquier[ligneInitiale][colonneInitiale];
            echiquier[ligneInitiale][colonneInitiale] = null;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Tente de déplacer une pièce d'une position à une autre sur l'échiquier.
     * Le déplacement peut échouer pour plusieurs raisons, selon les règles du
     * jeu d'échecs. Par exemples :
     *  Une des positions n'existe pas;
     *  Il n'y a pas de pièce à la position initiale;
     *  La pièce de la position initiale ne peut pas faire le mouvement;
     *  Le déplacement met en échec le roi de la même couleur que la pièce.
     *
     * @param initiale Position la position initiale
     * @param finale Position la position finale
     *
     * @return boolean true, si le déplacement a été effectué avec succès, false sinon
     */
    public boolean deplace(Position initiale, Position finale) {
        boolean succes = false;

        //Les positions initiale et finale sont valides
        if (positionInvalides(initiale) || positionInvalides(finale)){
            return false;
        }

        //piece a deplacer et position finale
        int colonneInitiale = initiale.getColonne()-'a';
        int ligneInitiale = -(initiale.getLigne() - 8);
        int colonneFinale = finale.getColonne()-'a';
        int ligneFinale = -(finale.getLigne() - 8);

        Piece pieceInitial = echiquier[ligneInitiale][colonneInitiale];
        Piece pieceFinal = echiquier[ligneFinale][colonneFinale];

        //Verifier si:
        // - Il y a bien une pièce à déplacer à la position initiale
        // - La couleur de la pièce à déplacer possède bien la couleur correspondant au jour qui a la main
        // - Il n’y a pas à la position finale une pièce de même couleur que la pièce à déplacer;
        if (pieceInitial == null || pieceInitial.getCouleur() != tour || pieceFinal.getCouleur() == tour){
            return false;
        }

        //Si le joueur veut faire un roque, verifier si les conditions sont bien réunies.
        boolean estUnRoi = pieceInitial instanceof Roi;
        boolean roqueDeplacement = finale.estSurLaMemeLigneQue(initiale) && (colonneInitiale - 2 == colonneFinale || colonneInitiale + 2 == colonneFinale);

        if (estUnRoi && roqueDeplacement) {
            succes = deplaceRoque(colonneInitiale, colonneFinale, ligneInitiale, ligneFinale);
        } else {
            //verifier si effectue le deplacement est possible
            if (!pieceInitial.peutSeDeplacer(initiale, finale, echiquier)) {
                return false;
            }

            //effectuer le deplacement
            echiquier[ligneFinale][colonneFinale] = echiquier[ligneInitiale][colonneInitiale];
            echiquier[ligneInitiale][colonneInitiale] = null;
            succes = true;
        }
        return succes;
    }

    /**
     * Vérifie si un roi est en échec et, si oui, retourne sa couleur sous forme
     * d'un caractère n ou b.
     * Si la couleur du roi en échec est la même que celle de la dernière pièce
     * déplacée, le dernier déplacement doit être annulé.
     * Les 2 rois peuvent être en échec en même temps. Dans ce cas, la méthode doit
     * retourner la couleur de la pièce qui a été déplacée en dernier car ce
     * déplacement doit être annulé.
     *
     * @return char Le caractère n, si le roi noir est en échec, le caractère b,
     * si le roi blanc est en échec, tout autre caractère, sinon.
     */
    public char estEnEchec() {
            throw new NotImplementedException();
    }
    /**
     * Retourne la couleur n ou b du joueur qui a la main.
     *
     * @return char la couleur du joueur à qui c'est le tour de jouer.
     */
    public char getTour() {
        return tour;
    }
    /**
     * Retourne l'alias du premier joueur.
     * @return String alias du premier joueur.
     */
    public String getAliasJoueur1() {
        return aliasJoueur1;
    }
    /**
     * Modifie l'alias du premier joueur.
     * @param aliasJoueur1 String nouvel alias du premier joueur.
     */
    public void setAliasJoueur1(String aliasJoueur1) {
        this.aliasJoueur1 = aliasJoueur1;
    }
    /**
     * Retourne l'alias du deuxième joueur.
     * @return String alias du deuxième joueur.
     */
    public String getAliasJoueur2() {
        return aliasJoueur2;
    }
    /**
     * Modifie l'alias du deuxième joueur.
     * @param aliasJoueur2 String nouvel alias du deuxième joueur.
     */
    public void setAliasJoueur2(String aliasJoueur2) {
        this.aliasJoueur2 = aliasJoueur2;
    }
    /**
     * Retourne la couleur n ou b du premier joueur.
     * @return char couleur du premier joueur.
     */
    public char getCouleurJoueur1() {
        return couleurJoueur1;
    }
    /**
     * Retourne la couleur n ou b du deuxième joueur.
     * @return char couleur du deuxième joueur.
     */
    public char getCouleurJoueur2() {
        return couleurJoueur2;
    }
}
package com.echecs;

import com.echecs.pieces.*;
import com.echecs.util.EchecsUtil;
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
     * Si la tour a deja ete bougee
     */
    private boolean tourBougee;

    /**
     * La couleur de celui à qui c'est le tour de jouer (n ou b).
     */
    private char tour = 'b'; //Les blancs commencent toujours
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

        //Les positions initiale et finale sont valides
        boolean positionsInvalides = initiale.getLigne() > 8 || initiale.getLigne() < 1 ||
                                    finale.getLigne() > 8 || finale.getLigne() < 1 ||
                                    initiale.getColonne() > 'h' || initiale.getColonne() < 'a' ||
                                    finale.getColonne() > 'h' || finale.getColonne() < 'a';
        if (positionsInvalides){
            return false;
        }

        //piece a deplacer et position finale
        int x1 = initiale.getColonne()-97;
        int y1 = -(initiale.getLigne() - 8);
        int x2 = finale.getColonne()-97;
        int y2 = -(finale.getLigne() - 8);

        Piece pInitial = echiquier[y1][x1];
        Piece pFinal = echiquier[y2][x2];

        //Il y a bien une pièce à déplacer à la position initiale;
        if (pInitial == null){
            return false;
        }

        //La couleur de la pièce à déplacer possède bien la couleur correspondant au jour qui a la main;
        if (pInitial.getCouleur() != tour){
            return false;
        }

        //Il n’y a pas à la position finale une pièce de même couleur que la pièce à déplacer;
        if (pFinal.getCouleur() == tour){
            return false;
        }

        //Si le joueur veut faire un roque, les conditions sont bien réunies.
        boolean estUnRoi = pInitial instanceof Roi;
        boolean roqueDeplacement = finale.estSurLaMemeLigneQue(initiale) && (x1 - 2 == x2 || x1 + 2 == x2);

        if (estUnRoi && roqueDeplacement) {

            //si le roi ou la tour ont deja bouges, roque invalide
            if (roiBouge || tourBougee){
                return false;
            }

            //verifie que les cases sont vides
            //pour la grande roque
            if (x2 < x1) {
                //verifie qu'entre la colone b(1) et e(4), les cases sont vides
                for (int i = 1; i < 4; i++) {
                    if (echiquier[y1][i] != null) {
                        return false;
                    }
                }
            }

            //pour la petite roque
            else {
                //verifie qu'entre la colone f(5) et h(8), les cases sont vides
                for (int i = 5; i < 8; i++) {
                    if (echiquier[y1][i] != null) {
                        return false;
                    }
                }
            }
        }
        return pInitial.peutSeDeplacer(initiale, finale, echiquier);
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
package com.echecs;

import com.echecs.pieces.*;

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

        //assigner de manier random une couleur aux joueurs
        Random rand = new Random();
        if (rand.nextInt(2) == 0) {
            couleurJoueur1 = 'b';
            couleurJoueur2 = 'n';
        } else {
            couleurJoueur1 = 'n';
            couleurJoueur2 = 'b';
        }

        //assigner les attributs de bases
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
     *TBD
     *
     * @param position
     * @return
     */
    private boolean positionInvalides(Position position){
        return position.getLigne() > 8 || position.getLigne() < 1 || position.getColonne() > 'h' || position.getColonne() < 'a';
    }

    /**
     *TBD
     *
     * @param ligneInitiale
     * @param debut
     * @param fin
     * @return
     */
    private boolean cheminEnLigneEstVide(int ligneInitiale, int debut, int fin){
        for (int i = debut; i < fin; i++) {
            if (echiquier[ligneInitiale][i] != null) {
                return false;
            }
        }
        return true;
    }

    /**
     *TBD
     *
     * @param colonneInitial
     * @param colonneFinale
     * @param ligne
     * @return
     */
    private boolean mouvementRoqueValide(int colonneInitial, int colonneFinale, int ligne){
        if (colonneFinale < colonneInitial) {
            return !tour1Bougee && cheminEnLigneEstVide(ligne, 1, 4);
        } else {
            return !tour2Bougee && cheminEnLigneEstVide(ligne, 5, 8);
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

        //Verifie si les positions initiale et finale sont valides
        if (positionInvalides(initiale) || positionInvalides(finale)){
            return false;
        }

        //Definir les pieces a deplacer et les lignes et colonnes
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
        boolean deplacementEnRoque = finale.estSurLaMemeLigneQue(initiale) &&
                (colonneInitiale - 2 == colonneFinale || colonneInitiale + 2 == colonneFinale);

        // verifier que le deplacement est valide
        //en roque
        if (estUnRoi && deplacementEnRoque) {
            //impossible si le deplacement ne se fait pas en roque
            if (!mouvementRoqueValide(colonneInitiale, colonneFinale, ligneInitiale)){
                return false;
            }
        //ou pour tout autre deplacement
        } else {
            //verifier que le deplacement est possible
            if (!pieceInitial.peutSeDeplacer(initiale, finale, echiquier)){
                return false;
            }
        }

        //effectuer le deplacement
        //la derniere ligne dependemment de la couleur
        int derniereLigne;
        if (tour == 'b') {
            derniereLigne = 0;
        } else {
            derniereLigne = 7;
        }

        //Au cas ou le deplacement n'est pas valide car il mettrait le roi en echec
        Piece pieceCapture = echiquier[ligneFinale][colonneFinale];

        //pour le cas specifique de promotion de pion en dame
        if (pieceInitial instanceof Pion && ligneFinale == derniereLigne){
            echiquier[ligneFinale][colonneFinale] = new Dame(tour);
        } else {
            //sinon, deplacement normal
            echiquier[ligneFinale][colonneFinale] = echiquier[ligneInitiale][colonneInitiale];
        }

        //supprimer la piece de la position initiale
        echiquier[ligneInitiale][colonneInitiale] = null;

        if (estEnEchec()==tour){
            echiquier[ligneInitiale][colonneInitiale] = echiquier[ligneFinale][colonneFinale];
            echiquier[ligneFinale][colonneFinale] = pieceCapture;
            return false;
        }

        //deplacement reussi
        changerTour();
        return true;
    }

    /**
     *TBD
     *
     * @param couleur
     * @return
     */
    private Position trouveRoi(char couleur){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                if (echiquier[i][j] instanceof Roi && echiquier[i][j].getCouleur() == couleur){
                    char colonne = (char) (j + 'a');
                    byte ligne = (byte) -(i-8);
                    return new Position(colonne,ligne);
                }
            }
        }
        return null;
    }

    /**
     *TBD
     *
     * @param couleur
     * @param positionRoi
     * @return
     */
    private boolean verifieEchec(char couleur, Position positionRoi){

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){

                Piece piece = echiquier[i][j];

                if (piece != null){

                    char colonne = (char) (j + 'a');
                    byte ligne = (byte) -(i-8);
                    Position position = new Position(colonne, ligne);

                    if (piece.peutSeDeplacer(position, positionRoi, echiquier)){
                        return true;
                    }
                }
            }
        }

        return false;
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
        Position positionRoiB = trouveRoi('b');
        Position positionRoiN = trouveRoi('n');
        char couleurEnEchec;

        if (verifieEchec('b', positionRoiB)){
            couleurEnEchec = 'b';
        } else if (verifieEchec('n', positionRoiN)) {
            couleurEnEchec = 'n';
        } else {
            couleurEnEchec = 'x';
        }

        return couleurEnEchec;
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
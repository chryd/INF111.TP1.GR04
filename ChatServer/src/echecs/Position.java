package echecs;

/**
 * Représente une position sur un échiquier de jeu d'échecs. Les lignes de
 * l'échiquier sont numérotées de 8 à 1 et les colonnes de a à h.
 * Cette classe fournit quelques méthodes de comparaison de 2 positions :
 * sont-elles voisines ? sur la même ligne ? colonne ? diagonale ?
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class Position {
    private char colonne;  //a à h
    private byte ligne;    //0 à 7

    /**
     * Crée une position correspondant à une case d'un échiquier de jeu d'échecs.
     *
     * @param colonne char Colonne a à h de la case.
     * @param ligne byte Ligne 8 à 1 de la case.
     */
    public Position(char colonne, byte ligne) {
        this.colonne = colonne;
        this.ligne = ligne;
    }

    public char getColonne() {
        return colonne;
    }

    public byte getLigne() {
        return ligne;
    }

    /**
     * Indique si 2 positions sont voisines sur un échiquier.
     *
     * @param p Position La position à comparer avec this.
     * @return boolean true si les 2 positions sont voisines, false sinon.
     */
    public boolean estVoisineDe(Position p) {
        boolean voisineColonne;
        boolean voisineLigne;
        boolean notSame;

        voisineColonne = (this.colonne <= p.getColonne() + 1) && (this.colonne >= p.getColonne() - 1);
        voisineLigne = (this.ligne <= p.getLigne() + 1) && (this.ligne >= p.getLigne() - 1);
        notSame = !(this.colonne == p.getColonne() && this.ligne == p.getLigne());

        return voisineColonne && voisineLigne && notSame;
    }
    /**
     * Indique si 2 positions sont sur la même ligne sur un échiquier.
     *
     * @param p Position La position à comparer avec this.
     * @return boolean true si les 2 positions sont sur la même ligne, false sinon.
     */
    public boolean estSurLaMemeLigneQue(Position p) {
        return this.ligne == p.getLigne();
    }
    /**
     * Indique si 2 positions sont sur la même colonne sur un échiquier.
     *
     * @param p Position La position à comparer avec this.
     * @return boolean true si les 2 positions sont sur la même colonne, false sinon.
     */
    public boolean estSurLaMemeColonneQue(Position p) {
        return this.colonne == p.getColonne();
    }
    /**
     * Indique si 2 positions sont sur la même diagonale sur un échiquier.
     *
     * @param p Position La position à comparer avec this.
     * @return boolean true si les 2 positions sont sur la même diagonale, false sinon.
     */
    public boolean estSurLaMemeDiagonaleQue(Position p) {
        return Math.abs(this.colonne - p.getColonne()) == Math.abs(this.ligne - p.getLigne());
    }

    //A des fins de test
    @Override
    public String toString() {
        return "Position{" +
                "colonne=" + colonne +
                ", ligne=" + ligne +
                '}';
    }

    //A des fins de test
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (colonne != position.colonne) return false;
        return ligne == position.ligne;
    }

    //A des fins de test
    @Override
    public int hashCode() {
        int result = colonne;
        result = 31 * result + (int) ligne;
        return result;
    }
}

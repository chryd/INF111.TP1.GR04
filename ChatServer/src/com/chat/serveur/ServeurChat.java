package com.chat.serveur;

import com.chat.commun.net.Connexion;

import java.util.*;

/**
 * Cette classe �tend (h�rite) la classe abstraite Serveur et y ajoute le n�cessaire pour que le
 * serveur soit un serveur de chat.
 *
 * @author Abdelmoum�ne Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-15
 */
public class ServeurChat extends Serveur {

    /**
     * Cr�e un serveur de chat qui va �couter sur le port sp�cifi�.
     *
     * @param port int Port d'�coute du serveur
     */
    public ServeurChat(int port) {
        super(port);
    }

    @Override
    public synchronized boolean ajouter(Connexion connexion) {
        String hist = this.historique();
        if ("".equals(hist)) {
            connexion.envoyer("OK");
        }
        else {
            connexion.envoyer("HIST " + hist);
        }
        return super.ajouter(connexion);
    }
    /**
     * Valide l'arriv�e d'un nouveau client sur le serveur. Cette red�finition
     * de la m�thode h�rit�e de Serveur v�rifie si le nouveau client a envoy�
     * un alias compos� uniquement des caract�res a-z, A-Z, 0-9, - et _.
     *
     * @param connexion Connexion la connexion repr�sentant le client
     * @return boolean true, si le client a valid� correctement son arriv�e, false, sinon
     */
    @Override
    protected boolean validerConnexion(Connexion connexion) {

        String texte = connexion.getAvailableText().trim();
        char c;
        int taille;
        boolean res = true;
        if ("".equals(texte)) {
            return false;
        }
        taille = texte.length();
        for (int i=0;i<taille;i++) {
            c = texte.charAt(i);
            if ((c<'a' || c>'z') && (c<'A' || c>'Z') && (c<'0' || c>'9')
                    && c!='_' && c!='-') {
                res = false;
                break;
            }
        }
        if (!res)
            return false;
        for (Connexion cnx:connectes) {
            if (texte.equalsIgnoreCase(cnx.getAlias())) { //alias d�j� utilis�
                res = false;
                break;
            }
        }
        connexion.setAlias(texte);
        return true;
    }

    /**
     * Retourne la liste des alias des connect�s au serveur dans une cha�ne de caract�res.
     *
     * @return String cha�ne de caract�res contenant la liste des alias des membres connect�s sous la
     * forme alias1:alias2:alias3 ...
     */
    public String list() {
        String s = "";
        for (Connexion cnx:connectes)
            s+=cnx.getAlias()+":";
        return s;
    }
    /**
     * Retourne la liste des messages de l'historique de chat dans une cha�ne
     * de caract�res.
     *
     * @return String cha�ne de caract�res contenant la liste des alias des membres connect�s sous la
     * forme message1\nmessage2\nmessage3 ...
     */
    public Vector<String> historique = new Vector<String>();
    
    public String historique() {
    	String s = "";
    	if(historique != null && historique.size() > 0) {
    		Iterator<String> iterateur = historique.iterator();
 
	    	while (iterateur.hasNext()) {
	    	s =  s  + iterateur.next() + "\n" ; 	   
            }
        }
        return s;
    	}
    public void ajouterHistorique(String nouveauMessage) {
    	
    		historique.add(nouveauMessage);
    
    }
    /**
     * Méthode pour trouver un utilisateur specifique
     *
     * @param toFind String chaine de caractere du nom d'un alias a retrouver
     * @return Connexion la connexion representant l'utilisateur a retrouver
     */
    public Connexion findAlias(String toFind) {
        for (Connexion c : connectes) {
            if (c.getAlias().equals(toFind)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Méthode pour envoyer le message a tout les autres utilisateurs.
     *
     * @param str
     * @param aliasExpediteur
     */
    public void envoyerATousSauf(String str, String aliasExpediteur){

        for (Connexion utiliConnecter : connectes) {
            if (!Objects.equals(utiliConnecter.getAlias(), aliasExpediteur)) {
                utiliConnecter.envoyer(str);
            }
        }
    }

    /**
     * Methode pour trouver verifier si l'utilisateur recherche est connecte
     *
     * @param toFind String chaine de caractere representant l'alias de l'utilisateur a retrouver
     */
    public void checkIfConnected(String toFind) throws NonExistentUserException {
        if (this.findAlias(toFind)==null){
            throw new NonExistentUserException("Utilisateur non existant");
        }
    }

    /**
     * Cette classe etend(herite) la classe Exception afin de prendre en charge les cas d'exception ou l'utilisateur n'existe pas.
     */
    public class NonExistentUserException extends Exception {
        /**
         * Creer une exception pour le cas ou l'utilisateur n'existe pas
         *
         * @param errorMessage String chaine de caractere a afficher lorsque l'exception NonExistentUserException est genere
         */
        public NonExistentUserException(String errorMessage){
            super(errorMessage);
        }
    }
}

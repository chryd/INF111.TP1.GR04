package com.chat.serveur;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;
import java.util.*;

import static java.lang.Integer.MAX_VALUE;

/**
 * Cette classe repr�sente un gestionnaire d'�v�nement d'un serveur. Lorsqu'un serveur re�oit un texte d'un client,
 * il cr�e un �v�nement � partir du texte re�u et alerte ce gestionnaire qui r�agit en g�rant l'�v�nement.
 *
 * @author Abdelmoum�ne Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;
    private ArrayList<Invitation> invitationList;
    private ArrayList<SalonPrive> privateList;

    /**
     * Construit un gestionnaire d'�v�nements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire g�re des �v�nements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
        invitationList = new ArrayList<>();
        privateList = new ArrayList<>();
    }

    /**
     * M�thode de gestion d'�v�nements. Cette m�thode contiendra le code qui g�re les r�ponses obtenues d'un client.
     *
     * @param evenement L'�v�nement � g�rer.
     */
    @Override
    public void traiter(Evenement evenement) throws ServeurChat.NonExistentUserException {
        Object source = evenement.getSource();
        Connexion cnx;
        String msg, typeEvenement, aliasExpediteur, aliasArgs;
        SalonPrive salonPrive;
        Invitation invitation;
        ServeurChat serveur = (ServeurChat) this.serveur;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            System.out.println("SERVEUR-Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
                case "EXIT": //Ferme la connexion avec le client qui a envoy� "EXIT":

                    try {
                        //verifie que le nombre d'argument est adequat
                        evenement.checkArguments(0, 0);
                        cnx.envoyer("END");
                        serveur.enlever(cnx);
                        cnx.close();
                    } catch (IllegalArgumentException illegalArgumentException) { //dans le cas ou on a plus d'un evenement
                        System.out.println("Format attendu  la commande: 'EXIT'");
                    }
                    break;

                case "LIST": //Envoie la liste des alias des personnes connect�es :
                    try {
                        //verifie que le nombre d'argument est adequat
                        evenement.checkArguments(0, 0);
                        cnx.envoyer("LIST " + serveur.list());
                    } catch (IllegalArgumentException illegalArgumentException) { //dans le cas ou on a plus d'un evenement
                        System.out.println("Format attendu  la commande: 'LIST'");
                    }

                    break;

                case "JOIN": //Envoie une demande d'invitation ou rejoind un salon prive avec l'utilisateur soecifie
                    try {
                        //verifie que le nombre d'arguments est adequat
                        evenement.checkArguments(1, 1);

                        //Definir les alias
                        aliasExpediteur = cnx.getAlias();
                        aliasArgs = evenement.getArgument();

                        //verifie que l'utilisateur recherche existe
                        serveur.checkIfConnected(aliasArgs);

                        //Creer une nouvelle invitation
                        invitation = new Invitation(aliasExpediteur, aliasArgs);

                        //Si une invitation entre ces utilisateurs a deja ete lance
                        if (!invitationList.isEmpty() && invitationList.contains(invitation)) {

                            //Un message pour signifier qu'un salon prive est ouvert est envoye aux deux utilisateurs
                            cnx.envoyer("JOINOK " + aliasArgs);
                            serveur.findAlias(aliasArgs).envoyer("JOINOK " + aliasExpediteur);
                            privateList.add(new SalonPrive(aliasExpediteur, aliasArgs));

                            //L'invitation est retire de la liste d'invitation
                            invitationList.remove(invitation);
                        } else {
                            //sinon lancer une invitation
                            invitationList.add(invitation);
                            //Un message est envoye a l'invite pour lui signifier qu'on lui a envoye une invitation
                            serveur.findAlias(aliasArgs).envoyer("JOIN " + aliasExpediteur);
                        }
                    } catch (Exception e) {
                        if (e instanceof IllegalArgumentException) {
                            System.out.println("Format attendu  la commande: 'JOIN alias'");
                        } else if (e instanceof ServeurChat.NonExistentUserException) {
                            System.out.println("Utilisateur non-existant");
                        } else {
                            throw e;
                        }
                    }
                    break;

                case "DECLINE": //Refuser ou supprimer une demande d'invitation
                    try {
                        //verifie que le nombre d'arguments est adequat
                        evenement.checkArguments(1, 1);

                        //Definir les alias
                        aliasExpediteur = cnx.getAlias();
                        aliasArgs = evenement.getArgument();

                        //verifie que l'utilisateur recherche existe
                        serveur.checkIfConnected(aliasArgs);

                        //nouvelle invitation
                        invitation = new Invitation(aliasArgs, aliasExpediteur);

                        //Si l'invitation existe deja
                        if (invitationList.contains(invitation)) {

                            //si l'invitation ne vient pas de l'expediteur
                            if (!invitation.getAliasEmetteur().equals(aliasExpediteur)) {
                                //envoyer un message a l'expediteur afin d'indiquer que l'invitation est refusee
                                serveur.findAlias(aliasArgs).envoyer("DECLINE " + aliasExpediteur);
                            }

                            //retirer l'invitation de la liste
                            invitationList.remove(invitation);
                        }
                    } catch (Exception e){
                        if (e instanceof IllegalArgumentException) {
                            System.out.println("Format attendu  la commande: 'DECLINE alias");
                        } else if (e instanceof ServeurChat.NonExistentUserException) {
                            System.out.println("Utilisateur non-existant");
                        } else {
                            throw e;
                        }
                    }
                    break;

                case "PRV": //envoyer un message prive a l'utilisateur specifie si le salon existe deja
                    try {
                        evenement.checkArguments(1, MAX_VALUE);
                        //Diviser les arguments recu pour separer l'alias du message
                        String[] args = evenement.getArgument().split(" ", 2);

                        //definir les alias
                        aliasExpediteur = cnx.getAlias();
                        aliasArgs = args[0];

                        //verifie que l'utilisateur recherche existe
                        serveur.checkIfConnected(aliasArgs);

                        //definir le message
                        msg = aliasExpediteur + " (private)>> " + args[1];

                        //cree un nouveau salon sprive
                        salonPrive = new SalonPrive(aliasExpediteur, aliasArgs);

                        //si le salon est deja cree, envoyer le message a l'utilisateur specifie
                        if (privateList.contains(salonPrive)) {
                            serveur.findAlias(aliasArgs).envoyer(msg);
                        }

                    } catch (Exception e) {
                        if (e instanceof IllegalArgumentException) {
                            System.out.println("Format attendu  la commande: 'PRV alias message'");
                        } else if (e instanceof ServeurChat.NonExistentUserException) {
                            System.out.println("Utilisateur non-existant");
                        } else {
                            throw e;
                        }
                    }
                            break;

                case "INV": //Envoie la liste des alias des personnes qui on envoye une invitation au client :
                    try {
                        //verifie que le nombre d'argument est adequat
                        evenement.checkArguments(0,0);

                        //definir l'alias de l'expediteur
                        aliasExpediteur = cnx.getAlias();

                        String listDInvitation = "";
                        //pour les invitations dans la liste d'invitation
                        for (Invitation i : invitationList) {
                            //si l'expediteur est le recepteur de l'invitation
                            if (i.getAliasRecepteur().equals(aliasExpediteur)) {
                                listDInvitation += i.getAliasEmetteur() + ":"; //ajouter le nom de l'emeteur de l'invitation a la liste
                            }
                        }

                        //envoyer le liste au client
                        cnx.envoyer("INV " + listDInvitation);
                    } catch (IllegalArgumentException illegalArgumentException){
                        System.out.println("Format attendu  la commande: 'INV'");
                    }
                    break;

                case "QUIT": //quitter le salon prive
                    try {
                        //Verifie que le nombre d'arguments est adequat
                        evenement.checkArguments(1,1);

                        //definir les alias
                        aliasExpediteur = cnx.getAlias();
                        aliasArgs = evenement.getArgument();

                        //verifie que l'utilisateur recherche existe
                        serveur.checkIfConnected(aliasArgs);

                        //creer un salon prive
                        salonPrive = new SalonPrive(aliasExpediteur, aliasArgs);

                        //retirer de la liste le salon prive qui est a fermer
                        privateList.remove(salonPrive);

                        //enovoyer les messages approprie aux clients
                        cnx.envoyer("QUIT " + aliasArgs);
                        serveur.findAlias(aliasArgs).envoyer("QUIT " + aliasExpediteur);

                    } catch (Exception e) {
                        if (e instanceof IllegalArgumentException) {
                            System.out.println("Format attendu  la commande: 'QUIT alias'");
                        } else if (e instanceof ServeurChat.NonExistentUserException) {
                            System.out.println("Utilisateur non-existant");
                        } else {
                            throw e;
                        }
                    }
                    break;

                /*case "CHESS":
                    break;*/

                case "MSG": //Envoie la liste des alias des personnes connect�es :
                    aliasExpediteur = cnx.getAlias();
                    msg = aliasExpediteur + ">>" +evenement.getArgument();

                    serveur.envoyerATousSauf(msg, aliasExpediteur);
                    serveur.ajouterHistorique(msg);
                    break;
                    
                case "HIST": //affiche l'historique
                	//try 
                	{
                        //verifie que le nombre d'argument est adequat
                        //evenement.checkArguments(0, 0);
                        cnx.envoyer("HIST " + serveur.historique());
                  //  } catch (IllegalArgumentException illegalArgumentException) { //dans le cas ou on a plus d'un evenement
                  //      System.out.println("Format attendu  la commande: 'HIST'");
                		}
            
                    break;
                    
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = evenement.getType() + " " + evenement.getArgument().toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}
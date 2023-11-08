package com.chat.serveur;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;
import java.util.*;

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
    public void traiter(Evenement evenement) {
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
                    cnx.envoyer("END");
                    serveur.enlever(cnx);
                    cnx.close();
                    break;

                case "LIST": //Envoie la liste des alias des personnes connect�es :
                    cnx.envoyer("LIST " + serveur.list());
                    break;

                case "JOIN":
                    //Definir les alias
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = evenement.getArgument();
                    invitation = new Invitation(aliasExpediteur, aliasArgs);

                    //Si une invitation entre ces utilisateurs a deja ete lance
                    if (!invitationList.isEmpty() && invitationList.contains(invitation)){
                        cnx.envoyer("JOINOK " + aliasArgs);
                        serveur.findAlias(aliasArgs).envoyer("JOINOK " + aliasExpediteur);
                        privateList.add(new SalonPrive(aliasExpediteur, aliasArgs));
                        invitationList.remove(invitation);
                    } else { //sinon lancer une invitation
                        invitationList.add(invitation);
                        serveur.findAlias(aliasArgs).envoyer("JOIN " + aliasExpediteur);
                    }
                    break;

                case "DECLINE":
                    //Definir les alias
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = evenement.getArgument();
                    invitation = new Invitation(aliasArgs, aliasExpediteur);

                    //Si l'invitation existe deja
                    if(invitationList.contains(invitation)){
                        if (!invitation.getAliasEmetteur().equals(aliasExpediteur)){ //si l'invitation ne vient pas de l'expediteur
                            //envoyer un message a l'expediteur afin d'indiquer que l'invitation est refusee
                            serveur.findAlias(aliasArgs).envoyer("DECLINE " + aliasExpediteur);
                        }
                        invitationList.remove(invitation); //retirer l'invitation de la liste
                    }
                    break;

                case "PRV":
                    //
                    String[] args = evenement.getArgument().split(" ",2);

                    //definir les alias
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = args[0];
                    msg = aliasExpediteur + " (private)>> " + args[1];
                    salonPrive = new SalonPrive(aliasExpediteur, aliasArgs);

                    if (privateList.contains(salonPrive)){
                        serveur.findAlias(aliasArgs).envoyer(msg);
                    }
                    break;

                case "INV":
                    aliasExpediteur = cnx.getAlias();
                    String s = "";
                    for(Invitation i:invitationList) {
                        if(i.getAliasRecepteur().equals(aliasExpediteur)){
                            s+=i.getAliasEmetteur()+":";
                        }
                    }
                    cnx.envoyer("INV " + s);
                    break;

                case "QUIT":
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = evenement.getArgument();
                    salonPrive = new SalonPrive(aliasExpediteur, aliasArgs);

                    privateList.remove(salonPrive);
                    cnx.envoyer("QUIT " + aliasArgs);
                    serveur.findAlias(aliasArgs).envoyer("QUIT " + aliasExpediteur);
                    break;

                /*case "CHESS":
                    break;*/

                /**
                 * Le case permettant l'envoie du message à tous les utilisateurs hormis l'expéditeur.
                 */
                case "MSG": //Envoie la liste des alias des personnes connect�es :

                    aliasExpediteur = cnx.getAlias();
                    msg = aliasExpediteur + ">>" +evenement.getArgument();

                    serveur.envoyerATousSauf(msg, aliasExpediteur);
                    break;

                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = evenement.getType() + " " + evenement.getArgument().toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}
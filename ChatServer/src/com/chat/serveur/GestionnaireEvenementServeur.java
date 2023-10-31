package com.chat.serveur;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;
import java.util.*;

/**
 * Cette classe représente un gestionnaire d'événement d'un serveur. Lorsqu'un serveur reçoit un texte d'un client,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;
    private ArrayList<Invitation> invitationList;
    private ArrayList<SalonPrive> privateList;

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
        invitationList = new ArrayList<>();
        privateList = new ArrayList<>();
    }

    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un client.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx;
        String msg, typeEvenement, aliasExpediteur, aliasArgs;
        ServeurChat serveur = (ServeurChat) this.serveur;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            System.out.println("SERVEUR-Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
                case "EXIT": //Ferme la connexion avec le client qui a envoyé "EXIT":
                    cnx.envoyer("END");
                    serveur.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des alias des personnes connectées :
                    cnx.envoyer("LIST " + serveur.list());
                    break;

                case "JOIN":
                    // les alias
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = evenement.getArgument();
                    Invitation invitation = new Invitation(aliasExpediteur, aliasArgs);

                    //Si une invitation entre ces utilisateurs a deja ete lance
                    if (!invitationList.isEmpty() && invitationList.contains(invitation)){
                        traiter(new Evenement(cnx, "JOINOK", ""));
                    } else { //sinon lancer une invitation
                        invitationList.add(invitation);
                        cnx.envoyer("JOIN " + aliasArgs);
                    }
                    break;

                case "INV":
                    aliasExpediteur = cnx.getAlias();
                    String s = "";
                    for(Invitation i:invitationList) {
                        if(i.aPourInvite(aliasExpediteur)){
                            String e = i.getAliasEmetteur();
                            String r = i.getAliasRecepteur();
                            if (!e.equals(aliasExpediteur)){
                                s += e + ":";
                            } else {
                                s += r + ":";
                            }
                        }
                    }
                    cnx.envoyer("INV " + s);
                    break;

                case "JOINOK":
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = evenement.getArgument();
                    SalonPrive salonPrive = new SalonPrive(aliasExpediteur, aliasArgs);
                    privateList.add(salonPrive);
                    invitationList.remove(new Invitation(aliasExpediteur,aliasArgs));
                    break;

                case "DECLINE":
                    //Extraire les alias
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = evenement.getArgument();
                    Invitation invite = new Invitation(aliasArgs, aliasExpediteur);

                    if(invitationList.contains(invite)){
                        if (!invite.getAliasEmetteur().equals(aliasExpediteur)){ //si l'invitation ne vient pas de l'expediteur
                            cnx.envoyer("DECLINE aliasArgs");
                        }
                        invitationList.remove(invite);
                    }
                    break;

                case "PRV":
                    aliasExpediteur = cnx.getAlias();
                    String[] args = evenement.getArgument().split(" ");
                    aliasArgs = args[0];
                    msg = args[1];
                    salonPrive = new SalonPrive(aliasExpediteur, aliasArgs);

                    if (privateList.contains(salonPrive)){
                        cnx.envoyer("PRIV "+aliasArgs+msg);
                    }
                    break;

                case "QUIT":
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = evenement.getArgument();
                    salonPrive = new SalonPrive(aliasExpediteur, aliasArgs);

                    privateList.remove(salonPrive);
                    cnx.envoyer("QUIT");
                    break;

                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = evenement.getType() + " " + evenement.getArgument().toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}
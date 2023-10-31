package com.chat.serveur;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;

import java.lang.reflect.*;
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

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
        invitationList = new ArrayList<Invitation>();
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
        String msg, typeEvenement, aliasExpediteur, alias2;
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

                case "JOIN": //Inviter une personne alias2 à chatter en privé ou d'accepter l'invitation qui lui a été envoyer par alias2

                    //Extraire les alias
                    aliasExpediteur = cnx.getAlias();
                    alias2 = evenement.getArgument();
                    Invitation invitation = new Invitation(aliasExpediteur, alias2); //??? invitation cree pour non pas chaque evenement, mais chaque utilisateurs?
                        //ou stocker les invitations? car si une invitation entre les utilisateurs existent deja, then il faut seulement la consulter?

                    //Si une invitation a deja ete lance
                    if (invitationList.contains(invitation)){
                        SalonPrive salonPrive = new SalonPrive(aliasExpediteur, alias2); //ouvrir un nouveau salon prive pour ces utilisateurs
                    } else { //sinon lancer une invitation
                        cnx.envoyer("Invitation sent to" + alias2);
                        invitation.sendInvitation(); //lancer une invitation
                    }
                    break;

                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}
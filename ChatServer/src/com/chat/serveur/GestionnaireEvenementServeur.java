package com.chat.serveur;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;

<<<<<<< Updated upstream
=======
import java.util.*;

>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
    private ArrayList<Invitation> invitationList;
    private ArrayList<SalonPrive> privateList;
>>>>>>> Stashed changes

    /**
     * Construit un gestionnaire d'�v�nements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire g�re des �v�nements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
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
<<<<<<< Updated upstream
        String msg, typeEvenement, aliasExpediteur;
=======
        String msg, typeEvenement, aliasExpediteur, aliasArgs;
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
                //Ajoutez ici d�autres case pour g�rer d�autres commandes.
=======
                case "JOIN": //Inviter une personne aliasArgs � chatter en priv� ou d'accepter l'invitation qui lui a �t� envoyer par aliasArgs

                    //Extraire les alias
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = evenement.getArgument();
                    Invitation invitation = new Invitation(aliasExpediteur, aliasArgs);

                    //Si une invitation entre ces utilisateurs a deja ete lance
                    if (invitationList.contains(invitation)){
                        SalonPrive salonPrive = new SalonPrive(aliasExpediteur, aliasArgs); //ouvrir un nouveau salon prive pour ces utilisateurs
                        privateList.add(salonPrive);
                        invitationList.remove(invitation);
                    } else { //sinon lancer une invitation
                        cnx.envoyer("Invitation sent to" + aliasArgs);
                        invitationList.add(invitation);
                    }
                    break;

                case "DECLINE":
                    //Extraire les alias
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = evenement.getArgument();
                    Invitation invite = new Invitation(aliasArgs, aliasExpediteur);

                    if(invitationList.contains(invite)){
                        if (!invite.getAliasEmetteur().equals(aliasExpediteur)){ //si l'invitation ne vient pas de l'expediteur
                            msg = aliasArgs + "decline votre invitation";
                            //envoie le message a l'expediteur
                        }
                        invitationList.remove(invite);
                    }
                    break;

                case "INV":
                    aliasExpediteur = cnx.getAlias();
                    cnx.envoyer("Invitations:\n");
                    for(Invitation i:invitationList) {
                        String sender = i.getAliasRecepteur();
                        if (!sender.equals(aliasExpediteur)) {
                            cnx.envoyer(sender + "\n");
                        }
                    }
                    break;

                case "PRV":
                    aliasExpediteur = cnx.getAlias();
                    String[] args = evenement.getArgument().split(" ");
                    aliasArgs = args[0];
                    msg = args[1];
                    SalonPrive salonPrive = new SalonPrive(aliasExpediteur, aliasArgs);

                    if (privateList.contains(salonPrive)){
                        //envoyer le message prive
                    }
                    break;

                case "QUIT":
                    aliasExpediteur = cnx.getAlias();
                    String[] newArgs = evenement.getArgument().split(" ");
                    aliasArgs = newArgs[0];
                    msg = newArgs[1];
                    SalonPrive tempSalonPrive = new SalonPrive(aliasExpediteur, aliasArgs);

                    if (privateList.contains(tempSalonPrive)){
                        privateList.remove(tempSalonPrive);
                    }
                    break;
>>>>>>> Stashed changes

                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}
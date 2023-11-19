package com.chat.client;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;
import com.chat.serveur.ServeurChat;

/**
 * Cette classe représente un gestionnaire d'événement d'un client. Lorsqu'un client reçoit un texte d'un serveur,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementClient implements GestionnaireEvenement {
    private Client client;

    /**
     * Construit un gestionnaire d'événements pour un client.
     *
     * @param client Client Le client pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementClient(Client client) {
        this.client = client;
    }
    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un serveur.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx;
        String typeEvenement, arg;
        String[] membres;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
                case "END" : //Le serveur demande de fermer la connexion
                    client.deconnecter(); //On ferme la connexion
                    break;
                case "LIST" : //Le serveur a renvoyé la liste des connectés
                    arg = evenement.getArgument();
                    membres = arg.split(":");
                    System.out.println("\t\t"+membres.length+" personnes dans le salon :");
                    for (String s:membres)


                    break;

                case "JOIN": //Le serveur envoie une demande d'invitation
                    arg = evenement.getArgument();
                    System.out.println("Invitation reçu de " + arg);
                    break;

                case "INV" : //Le serveur a renvoyé la liste des invitations adressees au client
                    arg = evenement.getArgument();
                    membres = arg.split(":");//creer une liste a partir de la liste
                    int lMembres = membres.length;

                    if (lMembres == 0){
                        System.out.println("Pas d'invitation");
                    } else {
                        if (lMembres == 1) {
                            System.out.println("\t\t" + lMembres + " personne qui vous a invite :");
                        } else {
                            System.out.println("\t\t" + lMembres + " personnes qui vous ont invite :");
                        }
                        for (String s:membres) System.out.println("\t\t\t- "+s);
                    }
                    break;

                case "JOINOK": //Le serveur cree un nouveau chat prive avec l'utilisateur specifie
                    arg = evenement.getArgument();
                    System.out.println("Nouveau chat privé avec "+arg);
                    break;

                case "DECLINE" : //Le serveur supprime l'invitation avec l'utilisateur specifie
                    arg = evenement.getArgument();
                    System.out.println("Invitation refuse par " + arg);
                    break;

                case "QUIT" : //Le serveur ferme le salon avec l'utilisateur specifie
                    arg = evenement.getArgument();
                    System.out.println("Vous quittez le salon avec " + arg);
                    break;
                    
//                case "CHESS":
//                	try {
//                        //Verifie que le nombre d'arguments est adequat
//                        evenement.checkArguments(1,1);
//
//                        //definir les alias
//                        aliasExpediteur = cnx.getAlias();
//                        aliasArgs = evenement.getArgument();
//
//                        //verifie que l'utilisateur recherche existe
//                        serveur.checkIfConnected(aliasArgs);
//                        
//                        //v�rifie s'il y a d�ja une partie en cours
//                       // serveur.EtatPartieEchecs();
//
//                      //si le salon est deja cree, envoyer le message a l'utilisateur specifie
//                        if (privateList.contains(salonPrive)) {
//                            
//                            //enovoyer les messages approprie aux clients
//                            //cnx.envoyer("CHESS " + aliasArgs);
//                            serveur.findAlias(aliasArgs).envoyer("CHESS " + aliasExpediteur);
//                            // send cHESSOK if invitation is already there
//                            serveur.findAlias(aliasArgs).envoyer("CHESSOK " + aliasExpediteur + "B");
//                        }
//
//                    } catch (Exception e) {
//                        if (e instanceof IllegalArgumentException) {
//                            System.out.println("Format attendu  la commande: 'CHESS alias'");
//                        } else if (e instanceof ServeurChat.NonExistentUserException) {
//                            System.out.println("Utilisateur non-existant");
//                        } else {
//                            throw e;
//                        }
//                    }
//                		
//                	break;    
                    
                case "MOVE":
                	break;
                case "ABANDON":
                	break;
                    
                    
                default: //Afficher le texte recu :
                    System.out.println("\t\t\t."+evenement.getType()+" "+evenement.getArgument());
            }
        }
    }
}

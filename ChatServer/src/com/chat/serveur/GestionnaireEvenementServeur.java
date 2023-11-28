package com.chat.serveur;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;
import com.echecs.PartieEchecs;
import com.echecs.Position;
import java.util.*;

import static java.lang.Integer.MAX_VALUE;

/**
 * Cette classe reprï¿½sente un gestionnaire d'ï¿½vï¿½nement d'un serveur. Lorsqu'un serveur reï¿½oit un texte d'un client,
 * il crï¿½e un ï¿½vï¿½nement ï¿½ partir du texte reï¿½u et alerte ce gestionnaire qui rï¿½agit en gï¿½rant l'ï¿½vï¿½nement.
 *
 * @author Abdelmoumï¿½ne Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;
    private ArrayList<Invitation> invitationList;
    private ArrayList<SalonPrive> privateList;
    private char couleur_alias1;
    private char couleur_alias2;
    private String msg1;
    private String msg2;
    

    /**
     * Construit un gestionnaire d'ï¿½vï¿½nements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gï¿½re des ï¿½vï¿½nements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
        invitationList = new ArrayList<>();
        privateList = new ArrayList<>();
    }

    /**
     * Mï¿½thode de gestion d'ï¿½vï¿½nements. Cette mï¿½thode contiendra le code qui gï¿½re les rï¿½ponses obtenues d'un client.
     *
     * @param evenement L'ï¿½vï¿½nement ï¿½ gï¿½rer.
     */
    @Override
    public void traiter(Evenement evenement) throws ServeurChat.NonExistentUserException {
        Object source = evenement.getSource();
        Connexion cnx;
        String msg, typeEvenement, aliasExpediteur, aliasArgs;
        SalonPrive salonPrive = null;
        Invitation invitation;
        ServeurChat serveur = (ServeurChat) this.serveur;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            System.out.println("SERVEUR-Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
                case "EXIT": //Ferme la connexion avec le client qui a envoyï¿½ "EXIT":

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

                case "LIST": //Envoie la liste des alias des personnes connectï¿½es :
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
                        evenement.checkArguments(0, 0);

                        //definir l'alias de l'expediteur
                        aliasExpediteur = cnx.getAlias();

                        String listDInvitation = "";
                        //pour les invitations dans la liste d'invitation
                        for (Invitation i : invitationList) {
                            //si l'expediteur est le recepteur de l'invitation
                            if (i.getAliasRecepteur().equals(aliasExpediteur)) {
                                listDInvitation += i.getAliasEmetteur() + ":"; //ajouter le nom de l'emeteur de l'invitation a la liste
                            } else {
                                listDInvitation += i.getAliasRecepteur() + ":"; //ajouter le nom de l'expediteur de l'invitation a la liste
                            }
                        }

                        //envoyer le liste au client
                        cnx.envoyer("INV " + listDInvitation);
                    } catch (IllegalArgumentException illegalArgumentException){
                        System.out.println("illegalArgumentException : Format attendu  la commande: 'INV'");
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

                case "CHESS":
                	try {
                        //Verifie que le nombre d'arguments est adequat
                        evenement.checkArguments(1,1);

                        //definir les alias
                        aliasExpediteur = cnx.getAlias();
                        aliasArgs = evenement.getArgument();

                        //verifie que l'utilisateur recherche existe
                        serveur.checkIfConnected(aliasArgs);
                        
                        //vérifie s'il y a déja une partie en cours
                       // serveur.EtatPartieEchecs();
                        
                        //cree un nouveau salon sprive
                        salonPrive = new SalonPrive(aliasExpediteur, aliasArgs);
                        
                        //System.out.println("salon prive?: " + privateList.contains(salonPrive));  //debug
                        // si le salon est deja cree, envoyer le message a l'utilisateur specifie
                        if (privateList.contains(salonPrive)) {
                            
                            //enovoyer les messages approprie aux clients
                            //cnx.envoyer("CHESS " + aliasArgs);
                            serveur.findAlias(aliasArgs).envoyer("CHESS " + aliasExpediteur);
	                        
	                      //Creer une nouvelle invitation
	                        invitation = new Invitation(aliasExpediteur, aliasArgs);
	                        
	                        //System.out.println("is there an invitation?:" + !invitationList.isEmpty());//debug
	                        //System.out.println("does it contain invitation from person?: " + invitationList.contains(invitation)); //debug
	                        //Si une invitation entre ces utilisateurs a deja ete lance
	                        if (!invitationList.isEmpty() && invitationList.contains(invitation)) {
	                        	
	                        //	System.out.println("alias1: " + salonPrive.getAlias1());  //debug
	                        //	System.out.println("alias2: " + salonPrive.getAlias2()); //debug
	                        	
	                        	Random random;
	                        	random = new Random();
	                        	if (random.nextBoolean()) {
	                        		 couleur_alias1 = 'b';
	                        		 couleur_alias2 = 'n';
	                        	} else {
	                        		 couleur_alias1 = 'n';
	                        		 couleur_alias2 = 'b';
	                        	}
	                        	
	                        	PartieEchecs partie = new PartieEchecs();
	                        	
	                        	// assigne joueur1/2 aux alias
	                        	partie.setAliasJoueur1(salonPrive.getAlias1());
	                        	partie.assignerAuJoueur1(couleur_alias1);
	                        	partie.setAliasJoueur2(salonPrive.getAlias2());
	                        	
	                            //Un message pour identifier leur couleur envoye aux deux utilisateurs
	                            msg1 = "CHESSOK " + salonPrive.getAlias1() + " " + couleur_alias1;
	                            msg2 = "CHESSOK " + salonPrive.getAlias2() + " " + couleur_alias2;
	                            
		                        //System.out.println("msg1: " + msg1);  //debug
		                        //System.out.println("msg2: " + msg2);  //debug

	                          //si le salon est deja cree, envoyer le message au deux utilisateur du salonPrive
	                            //if (privateList.contains(salonPrive)) {
	                            	
	                                serveur.findAlias(salonPrive.getAlias1()).envoyer(msg1);
	                                serveur.findAlias(salonPrive.getAlias2()).envoyer(msg2);

	                           // }
	
	                            //L'invitation est retire de la liste d'invitation
	                            invitationList.remove(invitation);
	                        } else {
	                            //sinon lancer une invitation
	                            invitationList.add(invitation);
	                            //Un message est envoye a l'invite pour lui signifier qu'on lui a envoye une invitation
	            //                serveur.findAlias(aliasArgs).envoyer("CHESS " + aliasExpediteur);
	                        }
                        }
                        
                    } catch (Exception e) {
                        if (e instanceof IllegalArgumentException) {
                            System.out.println("Format attendu  la commande: 'CHESS alias'");
                        } else if (e instanceof ServeurChat.NonExistentUserException) {
                            System.out.println("Utilisateur non-existant");
                        } else {
                            throw e;
                        }
                    }
                		
                	break;
                
                case "MOVE":
                    //definir les alias
                    aliasExpediteur = cnx.getAlias();
                    aliasArgs = evenement.getArgument();
                    cnx.envoyer(aliasArgs);
                	// extraire position de l'argument pour les mouvements a verifier 
                	String arg = evenement.getArgument();
                	
                	// verifie le format accepte  (c3-e4, c3 e4 ou c3e4)
                	if ((arg.length() == 5 && (arg.contains("-")|| arg.contains(" ")) || arg.length() == 4 )) { 
                		
	                	// prend les deux premiers caracteres
                		//System.out.println(arg);// debug
//                		//System.out.println(arg.substring(1,2));// debug
                		//System.out.println(Byte.parseByte(arg.substring(1,2)));// debug
	                	Position posDebut = new Position( arg.charAt(0), Byte.parseByte(arg.substring(1,2)));
	                	// prend les deux derniers caracteres
	                	Position posFinal = new Position(arg.charAt(arg.length()-2), Byte.parseByte(arg.substring(arg.length()-1)));
	                	//System.out.println(arg.charAt(arg.length()-2));// debug
	                	//System.out.println(arg.substring(arg.length()-1));// debug
	                			
	                	salonPrive = new SalonPrive(aliasExpediteur, aliasArgs);
	                	PartieEchecs partie = new PartieEchecs();
	                			//salonPrive.getPartech();
	                	//System.out.println("debut: " + posDebut);  // debug
	                	//System.out.println("final: " + posFinal.toString());  // debug
	                	
	                	boolean works = partie.deplace(posDebut, posFinal);
	                	//System.out.println("works :" + works); //debug
	                	// works vérifie si déplacement fonctionne
	                	if (works) {
	                		//déplacement réussi donc change de tour
	                		if (partie.estEnEchec() != 'x')	
	                		{
	                			if (partie.estEnEchec() == couleur_alias1) {
	                				//Un message pour identifier leur couleur envoye aux deux utilisateurs
	                				msg1 = "ECHEC " + salonPrive.getAlias1();
	                			}
	                			else {
	                				msg1 = "ECHEC " + salonPrive.getAlias2();	
	                			}
	                        
	                			//System.out.println("To client: " + msg1);  //debug

	                			serveur.findAlias(salonPrive.getAlias1()).envoyer(msg1);
	                			serveur.findAlias(salonPrive.getAlias2()).envoyer(msg1);
	                	
	                		}
	                		msg1 = "MOVE" + arg.substring(0,2) + arg.substring(arg.length()-2);	
	    
		        			//System.out.println("to both client: " + msg1);  //debug
		        			serveur.findAlias(salonPrive.getAlias1()).envoyer(msg1);
		        			serveur.findAlias(salonPrive.getAlias2()).envoyer(msg1);
	
	                		//change le tour
	                		partie.changerTour();
	                    }
	                	else {
	                    	 aliasExpediteur = cnx.getAlias();
	                         aliasArgs = evenement.getArgument();
	                         
	                         serveur.findAlias(aliasArgs).envoyer("INVALID :déplacement invalide" );
	                    }
	                	
                	}
                	else {
                		//definir les alias
                        aliasExpediteur = cnx.getAlias();
                        aliasArgs = evenement.getArgument();
                        
                        serveur.findAlias(aliasArgs).envoyer("INVALID : structure de mouvement invalide" );
                	}
                	break;
                
                case "CHESSOK":
                	// start partie
                	break;
                	
                case "ABANDON": //Envoie la liste des alias des personnes connectï¿½es :
                    //System.out.println("ON entre dans abandon");
                    aliasExpediteur = cnx.getAlias();
                    Connexion receveur;
                    salonPrive = privateList.get(0);
                    if(salonPrive.getAlias1().equals(aliasExpediteur))
                    {
                        receveur = serveur.findAlias(salonPrive.getAlias2());
                        cnx.envoyer("Vous avez perdu la partie !");
                        receveur.envoyer("Vous avez gagner la partie !");
                    }
                    else {
                        receveur = serveur.findAlias(salonPrive.getAlias1());
                        cnx.envoyer("Vous avez perdu la partie !");
                        receveur.envoyer("Vous avez gagner la partie !");
                    }
                    salonPrive.setPartech(null);

                    break;
                    
                case "MSG": //Envoie la liste des alias des personnes connectï¿½es :
                    aliasExpediteur = cnx.getAlias();
                    msg = aliasExpediteur + ">>" +evenement.getArgument();

                    serveur.envoyerATousSauf(msg, aliasExpediteur);
                    serveur.ajouterHistorique(msg);
                    break;
                    
                case "HIST": //affiche l'historique
                	try
                	{
                        //verifie que le nombre d'argument est adequat
                        //evenement.checkArguments(0, 0);
                        cnx.envoyer("HIST " + serveur.historique());
                    } catch (IllegalArgumentException illegalArgumentException) { //dans le cas ou on a plus d'un evenement
                        System.out.println("Format attendu  la commande: 'HIST'");
                    }
            
                    break;
                    
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = evenement.getType() + " " + evenement.getArgument().toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }
}
package com.chat.client;

/**
 * Cette classe étend la classe Client pour lui ajouter des fonctionnalités
 * spécifiques au chat et au jeu d'échecs en réseau.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class ClientChat extends Client {
    //Cette classe est pour le moment vide et sera compléter dans le TP.
    /*  Les déplacements de pièces validés par le serveur, feront une MAJ de l'etat de la partie.  */

    EtatPartieEchecs etatPartieEchecs;

    public void setEtatPartieEchecs(EtatPartieEchecs deplacementValide ){
        this.etatPartieEchecs = deplacementValide;
    }

    public EtatPartieEchecs getEtatPartieEchecs(){
        return etatPartieEchecs;
    }

    /*  Initialiser un nouvel objet EtatPartieEchecs  */

    public void nouvellePartie()
    {
        this.etatPartieEchecs = new EtatPartieEchecs();
        
    }


}

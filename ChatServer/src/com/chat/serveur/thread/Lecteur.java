package com.chat.serveur.thread;

import com.chat.serveur.*;

/**
 * Cette interface représente un objet qui fournit une méthode de lecture, typiquement un client ou un serveur.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public interface Lecteur {
    /**
     * Méthode de lecture
     */
    void lire() throws ServeurChat.NonExistentUserException;
}

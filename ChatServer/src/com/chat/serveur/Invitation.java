package com.chat.serveur;

public class Invitation {
    private String aliasEmetteur;
    private String aliasRecepteur;

    public Invitation(String aliasEmetteur, String aliasRecepteur){
        this.aliasEmetteur = aliasEmetteur;
        this.aliasRecepteur = aliasRecepteur;
    }

    public String getAliasEmetteur() {
        return aliasEmetteur;
    }

    public String getAliasRecepteur() {
        return aliasRecepteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invitation that = (Invitation) o;
        return aliasEmetteur.equals(that.aliasEmetteur) && aliasRecepteur.equals(that.aliasRecepteur) ||
                aliasEmetteur.equals(that.aliasRecepteur) && aliasRecepteur.equals(that.aliasEmetteur);
    }
}

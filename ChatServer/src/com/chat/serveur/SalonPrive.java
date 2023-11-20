package com.chat.serveur;

import com.echecs.PartieEchecs;

public class SalonPrive {
    private String alias1;
    private String alias2;
    
    public PartieEchecs partech;
    
    public PartieEchecs getPartech() {
    	return partech;
    }
    
    public void setPartech(PartieEchecs c) {
    	this.partech = c;
    }
    
    public SalonPrive(String alias1, String invitedAlias){
        this.alias1 = alias1;
        this.alias2 = invitedAlias;
    }

    public String getAlias1() {
        return alias1;
    }

    public String getAlias2() {
        return alias2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalonPrive that = (SalonPrive) o;
        return alias1.equals(that.alias1) && alias2.equals(that.alias2) ||
                alias1.equals(that.alias2) && alias2.equals(that.alias1);
    }
}

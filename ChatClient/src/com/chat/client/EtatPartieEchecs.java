package com.chat.client;
import com.echecs.*;
public class EtatPartieEchecs {


    /*  stocke l’état de l’échiquier avec un attribut */
    char [][] etatEchiquier = new char[8][8];

    public void setetatEchiquier(){
        this.etatEchiquier = etatEchiquier;
    } 

    public char getetatEchiquier(char etatEchiquier){
        return etatEchiquier;
    }
    /*  Initialise l’échiquier à l’état initial d’une partie d’échecs.  */

    public EtatPartieEchecs(){

        for(int i=1; i<=6;i++)
        {
            for(int j=0; j<=7;j++)
            {
                //pion noir
                if(i==1)
                {
                    etatEchiquier[i][j]='p';
                }
                //pion blanc
                else if( i==6)
                {
                    etatEchiquier[i][j]='P';
                }
                //case vide
                else{
                    etatEchiquier[i][j]='.';
                }
            }
        }

        //piece blanc
        this.etatEchiquier [7][0] = 'T';
        this.etatEchiquier [7][1] = 'C';
        this.etatEchiquier [7][2] = 'F';
        this.etatEchiquier [7][3] = 'D';
        this.etatEchiquier [7][4] = 'R';
        this.etatEchiquier [7][5] = 'F';
        this.etatEchiquier [7][6] = 'C';
        this.etatEchiquier [7][7] = 'T';

        //piece noir
        this.etatEchiquier [0][0] = 't';
        this.etatEchiquier [0][1] = 'c';
        this.etatEchiquier [0][2] = 'f';
        this.etatEchiquier [0][3] = 'r';
        this.etatEchiquier [0][4] = 'd';
        this.etatEchiquier [0][5] = 'f';
        this.etatEchiquier [0][6] = 'c';
        this.etatEchiquier [0][7] = 't';
         
		/*
		 * PartieEchecs nouvellePartie = new PartieEchecs(); for (int i = 0; i < 8;
		 * i++){ for (int j = 0 ; j < 8; j++){ if (nouvellePartie.getEchiquier()[i][j]
		 * instanceof ){
		 * 
		 * } else if {
		 * 
		 * } } }
		 */

    }

    /*  Affiche l'état de la partie sous la forme matricielle  */

    public String toString(){
        int val = 1;
        String tab= "";
        for(int i =0; i<etatEchiquier.length+1;i++)
        {

            if(val == 9){
                tab = tab+" ";
            }
            else{
                tab = tab+val+" ";
            }

            for(int j =0; j<etatEchiquier[0].length; j++)
            {
                if(i==etatEchiquier.length){
                    char temp = (char) ('a'+j);
                    tab = tab+temp + " ";
                }
                else{
                    tab = tab + etatEchiquier[i][j]+ " ";
                }
            }
            tab= tab +"\n";
            val++;

        }
        return tab;
    }



}

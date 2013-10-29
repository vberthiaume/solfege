package com.example.solfege;

public class Piece {

    public  MainGauche maingauche;
    public  MainDroite maindroite;
    public int     tempo;
    public String  mode;
    String retour;
    
    public Piece() {
        

        maingauche  =   new MainGauche();
        maindroite  =   new MainDroite();
        this.setMode("maj");
        this.setTempo(90);
        
        
    }
    
    public String generePiece(){
        
       retour = "tabstave notation = true tablature = false\n"+maingauche.genereAccordAbc(2)+maindroite.genereAccordAbc(2);        
        
        return retour;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
        maindroite.setTempo(tempo);
        maingauche.setTempo(tempo);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
        maindroite.setMode(mode);
        maingauche.setMode(mode);
    }

}

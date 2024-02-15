package com.bmp4.solfege;

public class Piece {

    public  LeftHand maingauche;
    public  RightHand maindroite;
    public int     tempo;
    public String  mode;
    String retour;
    
    public Piece() {
        

        maingauche  =   new LeftHand();
        maindroite  =   new RightHand();
        this.setMode("maj");
        this.setTempo(90);
        
        
    }
    
    public String generePiece(){
        
       retour = "tabstave notation = true tablature = false clef = treble \n notes "+
       maindroite.genereAccordAbc(2)+"\n tabstave notation = true tablature = false clef = bass \n notes "+
               maingauche.genereAccordAbc(2);        
        
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

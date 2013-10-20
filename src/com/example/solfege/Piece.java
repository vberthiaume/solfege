package com.example.solfege;

public class Piece {

    public  MainGauche maingauche;
    public  MainDroite maindroite;
    int     tempo;
    String  mode;
    
    public Piece() {

        this.maingauche  =   new MainGauche();
        this.maindroite  =   new MainDroite();
        this.tempo       =   90;
        this.mode        =   "maj";
        
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}

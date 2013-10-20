package com.example.solfege;

public class MainDroite extends Piece{

    private int     veloout;
    private int     hauteur;
    private int     degre;
    private int[][] gammes;
    //TODO faire en sorte que ce tableau soit setté par sept curseurs sur l'interface (valeurs entre 0 et 10)
    private int[]   probas_Notes =   { 7,  3,  4,  5,  7,  4,  3 };
    
    public MainDroite() {

        this.veloout        =   60;
        this.hauteur        =   48;
        this.gammes         =   init_gammes();
    }

    /**
     * Prends un degré en entrée et renvoie les notes de la main droite
     * @param degre
     * @return String au format VEXTAB
     */
    public String genereAccordAbc(int degre){
    
        String temp,temp2;
        
        temp    = "";
        temp2   = "";
        temp2   = String.format( " ( ( ( 4 4 ) ( %d ) ) ", super.tempo);
        temp    = temp2;
        this.degre  =   degre;

        /******************************************************************/
        if(super.mode.equals("maj")){
            temp = rythmes(5,temp,0);
        
        }//if maj
        /******************************************************************/
        else{
            temp = rythmes_min(5,temp,0);
        
        }//if min   
        /******************************************************************/

        temp    += " ) ";
        return temp;


    }//droite
    
    
    String rythmes(int max_division, String temp,int init){

        String buffer = new String("");
        int test;

        if(init < max_division){
        
            //Il choisi si oui ou non on va diviser ce temps.
            int max = (int)Math.pow(2, init);
            for( int i = 0 ; i < max ; i++){
                
                test = indice_table(this.probas_Notes,init);
    
                //oui on le divise
                if(test>0){
                    temp = rythmes(max_division,temp,init+1);
                          }//iftest   
        
        
        //non on le divise pas
                else{
                    buffer = String.format( " ( %d/%d ( %d %d ) ) ",
                    plussz_decide_rest(16),
                    max,
                    100*(degre2midi(degre)+hauteur+12+gammes[degre][cherche_table(this.probas_Notes)]),
                    veloout);
                    
                    temp = buffer;
                    }//else
            }//for
        }//if<5             
        return temp;
    }//rythmes
    
    
    String rythmes_min(int max_division, String temp,int init){

        String buffer;
        int test;

        if(init < max_division){
        
            int max = (int)Math.pow(2, init);
            //Il choisi si oui ou non on va diviser ce temps.
            
            for( int i = 0 ; i < max ; i++){
            
                test = indice_table(this.probas_Notes,init);

                //oui on le divise
                if(test > 0){
                        temp = rythmes_min(max_division,temp,init+1);
                    }//iftest   
        
                //non on le divise pas
                else{
                buffer = String.format(" ( %d/%d ( %d %d ) ) ",
                plussz_decide_rest(16),
                max,
                100*(degre2midi(degre)+hauteur+12+gammes[degre+7][cherche_table(this.probas_Notes)]),
                veloout);
                
                temp = buffer;
                }
            }//for
        }//if init...             
        return temp;
    }//rythmes_min

    
    int indice_table(int[] table,int i){

        int temp, r;
        
        r = (int) Math.random()*6;
        temp = table[i]; 
        
        if( r < temp ){
            return 0;
            }
        else return 1;

    }
    
    
    /**
     * Décide si il faut mettre un silence ou pas selon une probabilité (plus le paramètre d'entrée est grand, plus la chance est faible).
     * @param proba
     * @return  -1 ou 1 
     */
    int plussz_decide_rest(int proba)
    {
        int rest = (int)Math.random()*proba;
        
        if(rest ==0){return -1;}
        else {return 1;}

    }

    
    /**
     * Prends un degré de gamme en entrée et renvoie l'équivalent midi.
     * @param int degre
     * @return  int midi
     */
    int degre2midi(int degre){

        if(super.mode.equals("maj")){ 
            switch(degre){
            case 0:
                return 0;
            case 1:
                return 2;
            case 2:
                return 4;
            case 3:
                return 5;
            case 4:
                return 7;
            case 5:
                return 9;
            case 6:
                return 11;
            }//switch
        }//Majeur

        else{
        switch(degre){
            case 0:
                return 0;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 5;
            case 4:
                return 7;
            case 5:
                return 8;
            case 6:
                return 10;
            }//switch
        }//else (Mineur)
        return 0;
    }
    
   
    /**
     * Prends une table en entrée et va chercher une valeur dedant selon la probabilité qu'elle tombe
     * @param table
     * @return int 
     */
    int cherche_table(int[] table){

        int i, size =table.length, sum =0, rand_nb = 0,temp = 0,indice =0;
        

            for (i = 0; i < size; i++) {
                sum = sum+table[i];
            }

            if(sum == 0){return(-1);}
            else{rand_nb = (int)Math.random()*(100*sum)+1;}

            for(i = 0; i<size;i++){
                indice = indice+table[i]*100;
                if( temp < rand_nb && rand_nb < indice+1){
                    return i;
                    }
                else{
                    temp  = temp + table[i]*100;
                    }
                
            }//for

        return i;
    }//cherche_table
    
    /**
     * Initialise les gammes des modes majeurs et mineurs
     * @return tableau à deux dimentions contenant les différentes gammes
     */
    int[][] init_gammes(){

        int i,j;


                int gammesinit[][] = new int[14][9] ;


                int ionien2[]       =        { 0,  2,  4,  5,  7,  9,  11,   12,  14 };
                int dorien2[]       =        { 0,  2,  3,  5,  7,  9,  10,   12,  14 };
                int phrygien2[]     =        { 0,  1,  3,  5,  7,  8,  10,   12,  13 };
                int lydien2[]       =        { 0,  2,  4,  6,  7,  9,  11,   12,  14 };
                int mixo2[]         =        { 0 , 2,  4,  5,  7,  9,  10,   12,  14 };
                int aeolien2[]      =        { 0 , 2,  3,  5,  7,  8,  10,   12,  14 };
                int locrien2[]      =        { 0 , 1,  3,  5,  6,  8,  10,   12,  13 };
                int ionienmineur[]  =        { 0 , 1,  4,  5,  7,  8,  10,   12,  13 };


                //gammesinit = (int**)calloc(14,sizeof(int*));
                //gammesinit[j] = (int*)calloc(9,sizeof(int));

                for(j=0;j<7;j++){
                    for(i=0;i<9;i++){

                        if(j==0){gammesinit[j][i] = ionien2[i];}
                        if(j==1){gammesinit[j][i] = dorien2[i];}
                        if(j==2){gammesinit[j][i] = phrygien2[i];}
                        if(j==3){gammesinit[j][i] = lydien2[i];}
                        if(j==4){gammesinit[j][i] = mixo2[i];}
                        if(j==5){gammesinit[j][i] = aeolien2[i];}
                        if(j==6){gammesinit[j][i] = locrien2[i];}
                    }//for
                }//for

                for(j=7;j<14;j++){
                    for(i=0;i<9;i++){

                        if(j==7  ){gammesinit[j][i] = aeolien2[i];}
                        if(j==8){gammesinit[j][i] = locrien2[i];}
                        if(j==9){gammesinit[j][i] = ionien2[i];}
                        if(j==10){gammesinit[j][i] = dorien2[i];}
                        if(j==11){gammesinit[j][i] = ionienmineur[i];}
                        if(j==12){gammesinit[j][i] = lydien2[i];}
                        if(j==13){gammesinit[j][i] = mixo2[i];}
                    }//for
                }//for
                return gammesinit;
        }


    

    protected void setVeloout(int veloout) {
        this.veloout = veloout;
    }

    protected void setHauteur(int hauteur) {
        this.hauteur = hauteur;
    }

}//class mainDroite
























































package com.example.solfege;

import java.util.Vector;

import javax.swing.JFrame;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends Activity {
	
	private static factorysample affichage;
	private static JFrame win = new JFrame("GuidoEngine Java Factory");	 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void onClick(View view) {
        //EditText editText = (EditText) findViewById(R.id.edit_message);
        //TextView textView = (TextView) findViewById(R.id.textView1);
    	//String message = editText.getText().toString();
    	//textView.setText(message);
//    	MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.piano_c1); // probably should NOT be using getApplicationContext(), but not sure what else to use
//    	mediaPlayer.start(); // no need to call prepare(); create() does that for you
    	
    	EditText editText1 = (EditText) findViewById(R.id.editText1);
    	EditText editText2 = (EditText) findViewById(R.id.editText2);
    	
    	editText1.getText();
    	
    	MainGauche mainGauche = new MainGauche();
    	
    	Vector<String> accord = mainGauche.genereAccord("maj", 1);
    	editText1.setText(accord.toString());
    	Vector[] container = new Vector[1];
    	container[1].add(accord);
    	
		affichage = new factorysample(container, 1);
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setBounds(10, 30, 500, 400);
	    win.add( affichage );
	    win.setVisible(true);
    	//editText2.setText(editText1.getText());
    	
    	
        
    }
    
}

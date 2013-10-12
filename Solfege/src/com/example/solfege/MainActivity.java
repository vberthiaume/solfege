package com.example.solfege;

import java.util.Vector;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;


public class MainActivity extends Activity {
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        WebView lWebView = (WebView)findViewById(R.id.webView1);
        lWebView.getSettings().setJavaScriptEnabled(true);
        
        lWebView.loadUrl("file:///android_asset/seb.html");

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
    	
    	
    	
        
    }
    
}

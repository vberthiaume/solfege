package com.example.solfege;

import java.util.Vector;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.webkit.JavascriptInterface;

public class MainActivity extends Activity {
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        WebView lWebView = (WebView)findViewById(R.id.webView1);
        lWebView.getSettings().setJavaScriptEnabled(true);
        
        //Injection du code de MainGauche dans le Javascript de la webView...
        lWebView.addJavascriptInterface(new MainGauche(), "maingauche");

        
        lWebView.loadUrl("file:///android_asset/seb.html");
        
        Button button = (Button) findViewById(R.id.new_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WebView lWebView = (WebView)findViewById(R.id.webView1);
                lWebView.loadUrl( "javascript:window.location.reload( true )" );             
            }
        });
        

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    /** Called when the user clicks the Send button */
    public void onClick(View view) {
    
        //MainGauche mainGauche = new MainGauche();
        //Vector<String> accord = mainGauche.genereAccord("maj", 1);
    }
    

    
}

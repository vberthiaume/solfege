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
        
        //load the 
        //lWebView.loadUrl("file:///android_asset/VexTab.htm");
        lWebView.loadUrl("file:///android_asset/VexFlowTutorial.htm");
        
        
        //THIS WORKS
        //lWebView.addJavascriptInterface(new JsObject(), "injectedObject");
        
        //NOT WORKING Injection du code de MainGauche dans le Javascript de la webView...
        lWebView.addJavascriptInterface(new MainGauche(), "maingauche");
        
        
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    
    public void androidButtonClick(View view) {
    
        MainGauche mainGauche = new MainGauche();
        String strAccord = mainGauche.genereAccordAbc(1);
    }

}





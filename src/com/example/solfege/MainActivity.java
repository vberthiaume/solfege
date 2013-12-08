package com.example.solfege;

import java.io.File;
import java.io.IOException;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.SeekBar;

public class MainActivity extends Activity {
	
	private static final String TAG = "Solfege";
	static final int SET_PROBABILITY_SETTINGS = 1;
	public final static String DEGREE_PROBABILITY = "com.example.solfege.DEGREE_PROBABILITY";
	public final static String RHYTHM_PROBABILITY = "com.example.solfege.RHYTHM_PROBABILITY";
	private PdUiDispatcher dispatcher;
	private RightHand rightHand;
	private LeftHand leftHand;
	private Thread thread;    
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//initialize right and left hands
		rightHand = new RightHand();
		leftHand = new LeftHand();
		
		// load the notation view
		WebView notationWebView = (WebView) findViewById(R.id.partitionHtml);
		notationWebView.getSettings().setJavaScriptEnabled(true);
		notationWebView.loadUrl("file:///android_asset/solfegeHtmlView.htm");
		notationWebView.addJavascriptInterface(leftHand, "lefthand");
		notationWebView.addJavascriptInterface(rightHand, "righthand");

		//Initialize PD path
		try {
			initPd();
			loadPatch();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			finish();
		}
	}
	


	
	

	private void initPd() throws IOException {
		// Configure the audio glue
		int sampleRate = AudioParameters.suggestSampleRate();
		PdAudio.initAudio(sampleRate, 0, 2, 8, true);

		// Create and install the dispatcher
		dispatcher = new PdUiDispatcher();
		PdBase.setReceiver(dispatcher);
	}

	private void loadPatch() throws IOException {
		File dir = getFilesDir();
		IoUtils.extractZipResource(getResources().openRawResource(R.raw.tuna), dir, true);
		File patchFile = new File(dir, "tuna.pd");
		int val = PdBase.openPatch(patchFile.getAbsolutePath());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onPlayButtonClick(View view) {
		
		int curRootNote = rightHand.getCurrentMidiRootNote();
		if (curRootNote!= -1){
			PdBase.sendFloat("midinote1", curRootNote);
			PdBase.sendBang("rootTrigger");			
		}
		
		int curGuessNote = rightHand.getCurrentMidiGuessNote();
		if (curGuessNote != -1){
			PdBase.sendFloat("midinote2", curGuessNote);
			PdBase.sendBang("guessTrigger");
		}

		
	}
	
	public void onSettingsButtonClick(View view) {
		
		//create an intent to start the settings activity
		Intent intent = new Intent(this, SettingsActivity.class);
		
		intent.putExtra(DEGREE_PROBABILITY, rightHand.getDegreeProbability());
		intent.putExtra(RHYTHM_PROBABILITY, rightHand.getRhythmProbability());
		
		//create the activity, asking for a result
	    //startActivity(intent);
		startActivityForResult(intent,  SET_PROBABILITY_SETTINGS);
		
	}
	
	//called when other activities end
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	//if the activity that ended what the settings activity
        if (requestCode == SET_PROBABILITY_SETTINGS) {
            if (resultCode == RESULT_OK) {
            	rightHand.setDegreeProbability(data.getIntArrayExtra(MainActivity.DEGREE_PROBABILITY));
            	rightHand.setRhythmProbability(data.getIntArrayExtra(MainActivity.RHYTHM_PROBABILITY));
            }
        }
    }

	@Override
	protected void onResume() {
		super.onResume();
		PdAudio.startAudio(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		PdAudio.stopAudio();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		PdAudio.release();
		PdBase.release();
	}
}

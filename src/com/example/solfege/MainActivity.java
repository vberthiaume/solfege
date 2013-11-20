package com.example.solfege;

import java.io.File;
import java.io.IOException;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.utils.IoUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;

public class MainActivity extends Activity {

	private static final String TAG = "Solfege";
	private PdUiDispatcher dispatcher;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		WebView lWebView = (WebView) findViewById(R.id.webView1);
		lWebView.getSettings().setJavaScriptEnabled(true);

		// load the
		// lWebView.loadUrl("file:///android_asset/VexTab.htm");
		lWebView.loadUrl("file:///android_asset/VexFlowTutorial.htm");

		lWebView.addJavascriptInterface(new MainGauche(), "maingauche");

		try {
			initPd();
			loadPatch();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
			finish();
		}

	}

	private void triggerNote(int n) {
		PdBase.sendFloat("midinote", n);
		PdBase.sendBang("trigger");
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
		IoUtils.extractZipResource(getResources().openRawResource(R.raw.tuner),
				dir, true);
		File patchFile = new File(dir, "tuner.pd");
		int val = PdBase.openPatch(patchFile.getAbsolutePath());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void androidButtonClick(View view) {

		// MainGauche mainGauche = new MainGauche();
		// String strAccord = mainGauche.genereAccordAbc(1);

		triggerNote(99); // E is MIDI note 40.

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

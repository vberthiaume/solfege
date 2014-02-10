package com.solfege.solfege;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button imageButton1;
	static final int SET_PROBABILITY_SETTINGS = 1;

	/*
	 * private final ServiceConnection pdConnection = new ServiceConnection() {
	 * 
	 * @Override public void onServiceConnected(ComponentName name, IBinder
	 * service) { pdService = ((PdService.PdBinder) service).getService(); try {
	 * initPd(); loadPatch(); } catch (IOException e) { Log.e(LOG_TAG,
	 * e.toString()); finish(); } }
	 * 
	 * @Override public void onServiceDisconnected(ComponentName name) { // this
	 * method will never be called } };
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageButton1 = (Button) findViewById(R.id.singButton);
		imageButton1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onSingButtonClick(v);
			}
		});
	}

	private void start() {
		/* if (!pdService.isRunning()) { */
		Intent intent = new Intent(MainActivity.this, MainActivity.class);
		/*
		 * pdService.startAudio(intent, R.drawable.icon, "Solfege",
		 * "Return to Solfege."); }
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void initSystemServices() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new PhoneStateListener() {
			/*
			 * @Override public void onCallStateChanged(int state, String
			 * incomingNumber) { if (pdService == null) return; if (state ==
			 * TelephonyManager.CALL_STATE_IDLE) { start(); } else {
			 * pdService.stopAudio(); } }
			 */
		}, PhoneStateListener.LISTEN_CALL_STATE);
	}

	public void onSingButtonClick(View view) {

		// DISPLAY AND PLAY

		// this is how to call javascript functions from here... it works!
		// notationWebView.loadUrl("javascript:createRoot()");

		// create an intent to start the settings activity
		Intent intent = new Intent(this, SingActivity.class);
		// create the activity, asking for a result
		startActivity(intent);
		// startActivityForResult(intent, SET_PROBABILITY_SETTINGS);
	}

	public void onHarmonyButtonClick(View view) {

		// create an intent to start the settings activity
		Intent intent = new Intent(this, SettingsActivity.class);

		/*
		 * intent.putExtra(DEGREE_PROBABILITY,
		 * rightHand.getDegreeProbability());
		 * intent.putExtra(RHYTHM_PROBABILITY,
		 * rightHand.getRhythmProbability());
		 */

		// create the activity, asking for a result
		// startActivity(intent);
		// startActivityForResult(intent, SET_PROBABILITY_SETTINGS);

	}

	// called when other activities end
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// if the activity that ended what the settings activity
		if (requestCode == SET_PROBABILITY_SETTINGS) {
			if (resultCode == RESULT_OK) {
				// rightHand.setDegreeProbability(data.getIntArrayExtra(SingActivity.DEGREE_PROBABILITY));
				// rightHand.setRhythmProbability(data.getIntArrayExtra(SingActivity.RHYTHM_PROBABILITY));
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		/* PdAudio.startAudio(this); */
	}

	@Override
	protected void onPause() {
		super.onPause();
		/* PdAudio.stopAudio(); */

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		/*
		 * unbindService(pdConnection); PdAudio.release(); PdBase.release();
		 */
	}
}
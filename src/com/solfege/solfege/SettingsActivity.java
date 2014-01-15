package com.solfege.solfege;


import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.widget.SeekBar;

public class SettingsActivity extends Activity {

	private SeekBar[] seekBarDegrees = new SeekBar[7];
	private SeekBar[] seekBarRhythms = new SeekBar[4];
	private int m_degreeProb[], m_rhythmProb[];

	@SuppressLint("NewApi")  //not sure we need this, but google tutorial says so
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		//get the intent that was used to start this activity
		Intent intent = getIntent();
		m_degreeProb = intent.getIntArrayExtra(MainActivity.DEGREE_PROBABILITY);
		m_rhythmProb = intent.getIntArrayExtra(MainActivity.RHYTHM_PROBABILITY);
		
		// initialize the seek bars for degree probability
		initVerticalSeekBars();
	}
	
	private void initVerticalSeekBars() {

		seekBarDegrees[0] = (VerticalSeekBar) findViewById(R.id.seekBarFirstDeg);
		seekBarDegrees[1] = (VerticalSeekBar) findViewById(R.id.seekBarSecondDeg);
		seekBarDegrees[2] = (VerticalSeekBar) findViewById(R.id.seekBarThirdDeg);
		seekBarDegrees[3] = (VerticalSeekBar) findViewById(R.id.seekBarFourthDeg);
		seekBarDegrees[4] = (VerticalSeekBar) findViewById(R.id.seekBarFifthDeg);
		seekBarDegrees[5] = (VerticalSeekBar) findViewById(R.id.seekBarSixthDeg);
		seekBarDegrees[6] = (VerticalSeekBar) findViewById(R.id.seekBarSeventhDeg);
		
		seekBarRhythms[0] = (VerticalSeekBar) findViewById(R.id.seekBarFirstRhythm);
		seekBarRhythms[1] = (VerticalSeekBar) findViewById(R.id.seekBarSecondRhythm);
		seekBarRhythms[2] = (VerticalSeekBar) findViewById(R.id.seekBarThirdRhythm);
		seekBarRhythms[3] = (VerticalSeekBar) findViewById(R.id.seekBarFourthRhythm);

		for (int iCurParam = 0; iCurParam < seekBarDegrees.length; ++iCurParam) {
			seekBarDegrees[iCurParam].setProgress(m_degreeProb[iCurParam]);
			seekBarDegrees[iCurParam].setOnSeekBarChangeListener(new OnVerticalSeekBarChangeListener());
			
			if (iCurParam < seekBarRhythms.length){
				seekBarRhythms[iCurParam].setProgress(m_rhythmProb[iCurParam]);
				seekBarRhythms[iCurParam].setOnSeekBarChangeListener(new OnVerticalSeekBarChangeListener());
			}
		}
	}
	
	private void getVerticalSeekBarProgress(){
		for (int iCurParam = 0; iCurParam < seekBarDegrees.length; ++iCurParam) {
			m_degreeProb[iCurParam] = seekBarDegrees[iCurParam].getProgress();
			
			if (iCurParam < seekBarRhythms.length){
				m_rhythmProb[iCurParam] = seekBarRhythms[iCurParam].getProgress();

			}
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		Intent resultIntent = new Intent();
		
		getVerticalSeekBarProgress();
		
		resultIntent.putExtra(MainActivity.DEGREE_PROBABILITY, m_degreeProb);
		resultIntent.putExtra(MainActivity.RHYTHM_PROBABILITY, m_rhythmProb);
	
		
		//setResult(RESULT_OK, resultIntent);		
		if (getParent() == null) {
		    setResult(Activity.RESULT_OK, resultIntent);
		}
		else {
		    getParent().setResult(Activity.RESULT_OK, resultIntent);
		}
		
		

	}
	
	
//  //default method, used for handling things if we have an action bar	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.settings, menu);
//		return true;
//	}

}

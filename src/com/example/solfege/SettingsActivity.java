package com.example.solfege;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.widget.SeekBar;

public class SettingsActivity extends Activity {

	private SeekBar[] seekBarDegrees = new SeekBar[7];
	private SeekBar[] seekBarRhythms = new SeekBar[4];

	@SuppressLint("NewApi")  //not sure we need this, but google tutorial says so
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		//get the intent that was used to start this activity
		Intent intent = getIntent();
		int degreeProb[] = intent.getIntArrayExtra(MainActivity.DEGREE_PROBABILITY);
		int rhythmProb[] = intent.getIntArrayExtra(MainActivity.RHYTHM_PROBABILITY);
		
		// initialize the seek bars for degree probability
		initVerticalSeekBars(degreeProb, rhythmProb);
	}
	
	private void initVerticalSeekBars(int degreeProb[], int rhythmProb[]) {

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
			seekBarDegrees[iCurParam].setProgress(degreeProb[iCurParam]);
			seekBarDegrees[iCurParam].setOnSeekBarChangeListener(new OnVerticalSeekBarChangeListener());
			
			if (iCurParam < seekBarRhythms.length){
				seekBarRhythms[iCurParam].setProgress(rhythmProb[iCurParam]);
				seekBarRhythms[iCurParam].setOnSeekBarChangeListener(new OnVerticalSeekBarChangeListener());
			}
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

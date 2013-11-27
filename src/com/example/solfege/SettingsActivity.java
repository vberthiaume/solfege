package com.example.solfege;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.SeekBar;

public class SettingsActivity extends Activity {

	private SeekBar[] seekBarNotes = new SeekBar[7];
	private SeekBar[] seekBarRhythms = new SeekBar[4];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		// initialize the seek bars for degree probability
		initVerticalSeekBars();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	private void initVerticalSeekBars() {

		seekBarNotes[0] = (VerticalSeekBar) findViewById(R.id.seekBarFirstDeg);
		seekBarNotes[1] = (VerticalSeekBar) findViewById(R.id.seekBarSecondDeg);
		seekBarNotes[2] = (VerticalSeekBar) findViewById(R.id.seekBarThirdDeg);
		seekBarNotes[3] = (VerticalSeekBar) findViewById(R.id.seekBarFourthDeg);
		seekBarNotes[4] = (VerticalSeekBar) findViewById(R.id.seekBarFifthDeg);
		seekBarNotes[5] = (VerticalSeekBar) findViewById(R.id.seekBarSixthDeg);
		seekBarNotes[6] = (VerticalSeekBar) findViewById(R.id.seekBarSeventhDeg);
		
		seekBarRhythms[0] = (VerticalSeekBar) findViewById(R.id.seekBarFirstRhythm);
		seekBarRhythms[1] = (VerticalSeekBar) findViewById(R.id.seekBarSecondRhythm);
		seekBarRhythms[2] = (VerticalSeekBar) findViewById(R.id.seekBarThirdRhythm);
		seekBarRhythms[3] = (VerticalSeekBar) findViewById(R.id.seekBarFourthRhythm);

		for (int iCurParam = 0; iCurParam < seekBarNotes.length; ++iCurParam) {
			seekBarNotes[iCurParam].setOnSeekBarChangeListener(new OnVerticalSeekBarChangeListener());
			
			if (iCurParam < seekBarRhythms.length){
				seekBarRhythms[iCurParam].setOnSeekBarChangeListener(new OnVerticalSeekBarChangeListener());
			}
		}

	}

}

package com.solfege.solfege;

import java.io.File;
import java.io.IOException;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.io.PdAudio;
import org.puredata.android.service.PdService;
import org.puredata.android.utils.PdUiDispatcher;
import org.puredata.core.PdBase;
import org.puredata.core.PdListener;
import org.puredata.core.utils.IoUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;


public class MainActivity extends Activity {
	
	private static final String LOG_TAG = "Solfege";
	static final int SET_PROBABILITY_SETTINGS = 1;
	public final static String DEGREE_PROBABILITY = "com.example.solfege.DEGREE_PROBABILITY";
	public final static String RHYTHM_PROBABILITY = "com.example.solfege.RHYTHM_PROBABILITY";
	private PdUiDispatcher dispatcher;
	private RightHand rightHand;
	private LeftHand leftHand;
	private WebView notationWebView;
	private PitchView pitchView;
	private PdService pdService = null;
	private int curRootNote, curGuessNote;
	private Button mainButton;
	private float m_fCurAmplitude = 0;
	
	public enum states {
	    INIT, 
	    GIVE_GUESS,
	    PLAY_ANSWER,
	    START_OVER
	}
	private states cur_state;
	
	//------------------------------ ANDROID STUFF --------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mainButton = (Button)findViewById(R.id.mainButton);
		mainButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				onMainButtonClick(v);
			}
       });

		//initialize right and left hands
		rightHand = new RightHand();
		leftHand = new LeftHand();
		
		//init state and init web view
		cur_state = states.INIT; 
		initWebView();	

		//Initialize PD
		initSystemServices();
		bindService(new Intent(this, PdService.class), pdConnection, BIND_AUTO_CREATE);
        pitchView = (PitchView) findViewById(R.id.pitch_view);
        pitchView.setCenterPitch(127);
        
//		//prepare for audio file recording
//      mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//      mFileName += "/audiorecordtest.3gp";
	}
	
	private void initWebView() {
		// load the notation view
		notationWebView = (WebView) findViewById(R.id.partitionHtml);
		notationWebView.loadUrl("file:///android_asset/solfegeHtmlView.htm");

		// enable javascript
		notationWebView.getSettings().setJavaScriptEnabled(true);
		//notationWebView.setBackgroundColor(Color.argb(1,234,238,221));
		notationWebView.addJavascriptInterface(leftHand, "leftHand");
		notationWebView.addJavascriptInterface(rightHand, "rightHand");
		notationWebView.addJavascriptInterface(this, "mainActivity");
		
		
		notationWebView.setWebChromeClient(new WebChromeClient() {
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.e(LOG_TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId());
				return true;
			}
		});
	}
	
	@JavascriptInterface
	public int getWebViewWidth(){
		if (notationWebView == null){
			notationWebView = (WebView) findViewById(R.id.partitionHtml);
		}
		return notationWebView.getWidth();
	}
	
	@JavascriptInterface
	public int getWebViewHeight(){
		if (notationWebView == null){
			notationWebView = (WebView) findViewById(R.id.partitionHtml);
		}
		return notationWebView.getHeight();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void onMainButtonClick(View v) {

		try {
			switch (cur_state) {
			case INIT:
				// create and display root note
				
				//get new root note
				curRootNote = rightHand.getNewRootNote();
				
				//display note in javascript
				notationWebView.loadUrl("javascript:createRoot()");
				
				// send note for PD to play
				PdBase.sendFloat("midinote1", curRootNote);
				PdBase.sendBang("rootTrigger");

				// prepare for next state, ask for guess note
				cur_state = states.GIVE_GUESS;
				// rename the android main button
				((Button) v).setText(R.string.mainButtonGiveGuess);

				break;

			case GIVE_GUESS:
				
				curGuessNote = rightHand.getNewGuessNote();
				// create and display guess note
				//notationWebView.loadUrl("javascript:createGuessNote(" + rightHand.getCurrentMidiGuessNote() + ")");
				notationWebView.loadUrl("javascript:createGuessNote()");

				// start recording
				pitchView.setCenterPitch(curGuessNote % 12);

				// prepare for next state, ask for answer
				cur_state = states.PLAY_ANSWER;
				// rename the android main button
				((Button) v).setText(R.string.mainButtonPlayAnswer);

				break;
			case PLAY_ANSWER:

				// play answer
				PdBase.sendFloat("midinote1", curGuessNote);
				PdBase.sendBang("rootTrigger");

				// prepare for next state, ask for answer
				cur_state = states.START_OVER;
				((Button) v).setText(R.string.mainButtonStartOver);
				break;
				
			case START_OVER:

				// reset webview
				rightHand.resetNotes();
				notationWebView.loadUrl("javascript:reset()");

				// prepare for next state
				cur_state = states.INIT;
				((Button) v).setText(R.string.mainButtonGiveRoot);

				// reset pitch to max
				pitchView.setCenterPitch(127);

				break;
			default:
				Log.e(LOG_TAG, "ERROR-WRONGSTATE");
				break;
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, e.toString());
			finish();
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
		cur_state = states.INIT; 
		mainButton.setText(R.string.mainButtonGiveRoot);
		initWebView();
		PdAudio.startAudio(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		PdAudio.stopAudio();
		
//        if (mRecorder != null) {
//            mRecorder.release();
//            mRecorder = null;
//        }
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unbindService(pdConnection);
//		PdAudio.release();
//		PdBase.release();
	}
	
	//------------------------------ PUREDATA STUFF --------------------------------------
	private void initPd() throws IOException {
		
		// Configure the audio glue
		int sampleRate;
		sampleRate = AudioParameters.suggestSampleRate();
		//IF EMULATOR USE THIS
		//TODO find way to test for emulator
		sampleRate = 8000;
		
		//PdAudio.initAudio(sampleRate, 0, 2, 8, true);
		pdService.initAudio(sampleRate, 1, 2, 10.0f);
		pdService.startAudio();
		start();

		// Create and install the dispatcher
		dispatcher = new PdUiDispatcher();
		dispatcher.addListener("pitch", new PdListener.Adapter() {
			@Override
			public void receiveFloat(String source, final float x) {
				if (m_fCurAmplitude > 60)
					pitchView.setCurrentPitch(x%12);
				else
					pitchView.setCurrentPitch(0);
			}
		});
		
		dispatcher.addListener("amplitude", new PdListener.Adapter() {
			@Override
			public void receiveFloat(String source, final float amplitude) {
				m_fCurAmplitude = amplitude;
			}
		});
		
		PdBase.setReceiver(dispatcher);
	}
	
	private final ServiceConnection pdConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			pdService = ((PdService.PdBinder) service).getService();
			try {
				initPd();
				loadPatch();
			} catch (IOException e) {
				Log.e(LOG_TAG, e.toString());
				finish();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// this method will never be called
		}
	};

	private void loadPatch() throws IOException {
		File dir = getFilesDir();
		IoUtils.extractZipResource(getResources().openRawResource(R.raw.solfege_s_trigger), dir, true);
		File patchFile = new File(dir, "solfege.pd");
		int val = PdBase.openPatch(patchFile.getAbsolutePath());
	}
	
	private void start() {
		if (!pdService.isRunning()) {
			Intent intent = new Intent(MainActivity.this,
					MainActivity.class);
			pdService.startAudio(intent, R.drawable.icon, "Solfege",
					"Return to Solfege.");
		}
	}
	
	private void initSystemServices() {
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state, String incomingNumber) {
				if (pdService == null)
					return;
				if (state == TelephonyManager.CALL_STATE_IDLE) {
					start();
				} else {
					pdService.stopAudio();
				}
			}
		}, PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	
//------------------------------------------ ARCHIVE, HOW TO RECORD WITH ANDROID ----------------------------	
	//for recording and playing sounds with android SDK
//	private MediaRecorder mRecorder = null;
//	private MediaPlayer   mPlayer = null;
//	private static String mFileName = null;
//	boolean mStartRecording = true;
//	boolean mStartPlaying = true;

//	private void onRecord(boolean start, View v) {
//        if (start) {
//            startRecording(v);
//        } else {
//            stopRecording();
//        }
//    }
//
//    private void onPlay(boolean start, View v) {
//        if (start) {
//            startPlaying(v);
//        } else {
//            stopPlaying();
//        }
//    }
//
//    private void startPlaying(final View v) {
//        mPlayer = new MediaPlayer();
//        try {
//            mPlayer.setDataSource(mFileName);
//            mPlayer.prepare();
//            mPlayer.start();
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                        	onPlaySoundButtonClick(v);
//                        }
//                    });
//                }
//            }, 3000); 
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "prepare() failed");
//        }
//    }
//
//    private void stopPlaying() {
//        mPlayer.release();
//        mPlayer = null;
//    }
  
//  public void onRecordSoundButtonClick(View v) {
//  onRecord(mStartRecording, v);
//  if (mStartRecording) {
//  	((Button) v).setText("Stop recording");
//  } else {
//  	((Button) v).setText("Start recording");
//  }
//  mStartRecording = !mStartRecording;
//}
//
//public void onPlaySoundButtonClick(View v) {
//  onPlay(mStartPlaying, v);
//  if (mStartPlaying) {
//  	((Button) v).setText("Stop playing");
//  } else {
//  	((Button) v).setText("Start playing");
//  }
//  mStartPlaying = !mStartPlaying;
//}
//
    //    private void startRecording(final View v) {
//        mRecorder = new MediaRecorder();
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        mRecorder.setOutputFile(mFileName);
//        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//
//        try {
//            mRecorder.prepare();
//        } catch (IOException e) {
//            Log.e(LOG_TAG, "prepare() failed");
//        }
//
//        mRecorder.start();
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                    	onRecordSoundButtonClick(v);
//                    }
//                });
//            }
//        }, 3000); 
//    }
//
//    private void stopRecording() {
//        mRecorder.stop();
//        mRecorder.release();
//        mRecorder = null;
//    }
}

package com.solfege.solfege;


import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
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
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.os.Environment;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.widget.Button;
import android.widget.TextView;




public class SingActivity extends Activity {
        
        private static final String LOG_TAG = "Solfege2";
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
                setContentView(R.layout.activity_sing);
                
                mainButton = (Button)findViewById(R.id.mainButton);
                mainButton.setOnClickListener(new OnClickListener()
        {
                        @Override
                        public void onClick(View v) {
                                // TODO Auto-generated method stub
                                onMainButtonClick(v);
                        }
       });
                
                cur_state = states.INIT; 


                //initialize right and left hands
                rightHand = new RightHand();
                leftHand = new LeftHand();
                
                // load the notation view
                notationWebView = (WebView) findViewById(R.id.partitionHtml);
                notationWebView.loadUrl("file:///android_asset/solfegeHtmlView.htm");
                
                //enable javascript
                notationWebView.getSettings().setJavaScriptEnabled(true);
                notationWebView.addJavascriptInterface(leftHand, "lefthand");
                notationWebView.addJavascriptInterface(rightHand, "righthand");
                notationWebView.setWebChromeClient(new WebChromeClient() {
                          public boolean onConsoleMessage(ConsoleMessage cm) {
                            Log.e(LOG_TAG, cm.message() + " -- From line " + cm.lineNumber() + " of " + cm.sourceId() );
                            return true;
                          }
                        });                


                //Initialize PD path
                bindService(new Intent(this, PdService.class), pdConnection, BIND_AUTO_CREATE);
        pitchView = (PitchView) findViewById(R.id.pitch_view);
        pitchView.setCenterPitch(127);
        
//                //prepare for audio file recording
//      mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
//      mFileName += "/audiorecordtest.3gp";


        }


/*        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                // Inflate the menu; this adds items to the action bar if it is present.
                getMenuInflater().inflate(R.menu.main, menu);
                return true;
        }*/
        
        public void onMainButtonClick(View v) {


                try {
                        switch (cur_state) {
                        case INIT:
                                // create and display root note
                                notationWebView.loadUrl("javascript:createRoot()");


                                // send note for PD to play
                                curRootNote = rightHand.getCurrentMidiRootNote();
                                PdBase.sendFloat("midinote1", curRootNote);
                                PdBase.sendBang("rootTrigger");


                                // prepare for next state, ask for guess note
                                cur_state = states.GIVE_GUESS;
                                // rename the android main button
                                ((Button) v).setText(R.string.mainButtonGiveGuess);


                                break;


                        case GIVE_GUESS:
                                // create and display guess note
                                notationWebView.loadUrl("javascript:createGuessNote()");


                                // start recording
                                curGuessNote = rightHand.getCurrentMidiGuessNote();
                                pitchView.setCenterPitch(curGuessNote % 12);


                                // prepare for next state, ask for answer
                                cur_state = states.PLAY_ANSWER;
                                // rename the android main button
                                ((Button) v).setText(R.string.mainButtonPlayAnswer);


                                break;
                        case PLAY_ANSWER:


                                // play answer
                                PdBase.sendFloat("midinote2", curGuessNote);
                                PdBase.sendBang("guessTrigger");


                                // prepare for next state, ask for answer
                                cur_state = states.START_OVER;
                                ((Button) v).setText(R.string.mainButtonStartOver);
                                break;
                                
                        case START_OVER:


                                // reset webview
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


                // int curRootNote = rightHand.getCurrentMidiRootNote();
                // if (curRootNote!= -1){
                // PdBase.sendFloat("midinote1", curRootNote);
                // PdBase.sendBang("rootTrigger");
                // }
                //
                // int curGuessNote = rightHand.getCurrentMidiGuessNote();
                // if (curGuessNote != -1){
                // PdBase.sendFloat("midinote2", curGuessNote);
                // PdBase.sendBang("guessTrigger");
                // pitchView.setCenterPitch(curGuessNote%12);
                // }
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
                    rightHand.setDegreeProbability(data.getIntArrayExtra(SingActivity.DEGREE_PROBABILITY));
                    rightHand.setRhythmProbability(data.getIntArrayExtra(SingActivity.RHYTHM_PROBABILITY));
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
                
//        if (mRecorder != null) {
//            mRecorder.release();
//            mRecorder = null;
//        }
        }


        @Override
        public void onDestroy() {
                super.onDestroy();
                unbindService(pdConnection);
//                PdAudio.release();
//                PdBase.release();
        }
        
        //------------------------------ PUREDATA STUFF --------------------------------------
        private void initPd() throws IOException {
                int sampleRate;
                // Configure the audio glue
                if ("google_sdk".equals( Build.PRODUCT )){
                sampleRate = 8000;
                } else {
                        sampleRate = AudioParameters.suggestSampleRate();
                }
                
                //PdAudio.initAudio(sampleRate, 0, 2, 8, true);
                pdService.initAudio(sampleRate, 1, 2, 10.0f);
                pdService.startAudio();
                start();


                // Create and install the dispatcher
                dispatcher = new PdUiDispatcher();
                dispatcher.addListener("pitch", new PdListener.Adapter() {
                        @Override
                        public void receiveFloat(String source, final float x) {
                                pitchView.setCurrentPitch(x%12);
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
                
        private void loadPatch() throws IOException {
            File dir = getFilesDir();
            IoUtils.extractZipResource(getResources().openRawResource(R.raw.toggle_pitch), dir, true);
            File patchFile = new File(dir, "solfege.pd");
            int val = PdBase.openPatch(patchFile.getAbsolutePath());
                }


                @Override
                public void onServiceDisconnected(ComponentName name) {
                        // this method will never be called
                }
        };



        
        private void start() {
                if (!pdService.isRunning()) {
                        Intent intent = new Intent(SingActivity.this,
                                        SingActivity.class);
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
        
        
}


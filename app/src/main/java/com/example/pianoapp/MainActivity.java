package com.example.pianoapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import org.billthefarmer.mididriver.MidiDriver;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MidiDriver.OnMidiStartListener, View.OnTouchListener,OnPianoListener, OnLoadAudioListener, SeekBar.OnSeekBarChangeListener,
        View.OnClickListener, OnPianoAutoPlayListener{

    private MidiDriver midiDriver;
    private PianoView pianoView;
    private byte[] event;
    private Button c4,d4,e4,f4,g4,a4,b4,c5,c4_b,eb4_b,f4_b,g4_b,bb4_b,c5_b,c5_c;
    private final int scrollProgress = 0;
    private final static float SEEKBAR_OFFSET_SIZE = -12;
    private final boolean isPlay = false;
    private final ArrayList<AutoPlayEntity> litterStarList = null;
    private static final long LITTER_STAR_BREAK_SHORT_TIME = 500;
    private static final long LITTER_STAR_BREAK_LONG_TIME = 1000;

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button leftArrowButton = findViewById(R.id.iv_left_arrow);
        SeekBar seekBar = findViewById(R.id.sb);
        Button rightArrowButton = findViewById(R.id.iv_right_arrow);
        Button musicButton = findViewById(R.id.iv_music);
        // Set click listeners for buttons

        leftArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle left arrow button click
                leftArrowButton.setOnClickListener(this);
            }
        });

        rightArrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle right arrow button click
                rightArrowButton.setOnClickListener(this);
            }
        });

        musicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle music button click
                musicButton.setOnClickListener(this);
            }
        });

        // Set seek bar change listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Handle seek bar progress change

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Handle seek bar touch start
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Handle seek bar touch stop
            }
        });

        c4 = (Button)findViewById(R.id.c4); // c4=chords name
        c4.setOnTouchListener(this);

        d4 = (Button)findViewById(R.id.d4);
        d4.setOnTouchListener(this);

        e4 = (Button)findViewById(R.id.e4);
        e4.setOnTouchListener(this);

        f4 = (Button)findViewById(R.id.f4);
        f4.setOnTouchListener(this);

        g4 = (Button)findViewById(R.id.g4);
        g4.setOnTouchListener(this);

        a4 = (Button)findViewById(R.id.a4);
        a4.setOnTouchListener(this);

        b4 = (Button)findViewById(R.id.b4);
        b4.setOnTouchListener(this);

        c5 = (Button)findViewById(R.id.c5);
        c5.setOnTouchListener(this);

        c4_b = (Button)findViewById(R.id.c4_b);
        c4_b.setOnTouchListener(this);

        eb4_b = (Button)findViewById(R.id.eb4_b);
        eb4_b.setOnTouchListener(this);

        f4_b = (Button)findViewById(R.id.f4_b);
        f4_b.setOnTouchListener(this);

        g4_b = (Button)findViewById(R.id.g4_b);
        g4_b.setOnTouchListener(this);

        bb4_b = (Button)findViewById(R.id.bb4_b);
        bb4_b.setOnTouchListener(this);

        c5_b = (Button)findViewById(R.id.c5_b);
        c5_b.setOnTouchListener(this);

        c5_c = (Button)findViewById(R.id.c5_c);
        c5_c.setOnTouchListener(this);
        // Instantiate the driver.
        midiDriver = new MidiDriver();
        // Set the listener.
        midiDriver.setOnMidiStartListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        midiDriver.start();

        // Get the configuration.
        int[] config = midiDriver.config();

        // Print out the details.
       /* Log.d(this.getClass().getName(), "maxVoices: " + config[0]);
        Log.d(this.getClass().getName(), "numChannels: " + config[1]);
        Log.d(this.getClass().getName(), "sampleRate: " + config[2]);
        Log.d(this.getClass().getName(), "mixBufferSize: " + config[3]);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        midiDriver.stop();
    }
    private void playNote(int noteon, int channel, int middle) {

        // Construct a note ON message for the middle C at maximum velocity on channel 1:
        event = new byte[3];
        event[0] = (byte) (noteon | channel);  // 0x90 = note On, 0x00 = channel 1
        event[1] = (byte) middle;  // eg:0x3C = middle C
        event[2] = (byte) 0x7F;  // 0x7F = the maximum velocity (127)

        // Internally this just calls write() and can be considered obsoleted:
        //midiDriver.queueEvent(event);

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

    }

    private void stopNote(int noteoff, int channel, int middle) {

        // Construct a note OFF message for the middle C at minimum velocity on channel 1:
        event = new byte[3];
        event[0] = (byte) (noteoff | channel);  // 0x80 = note Off, 0x00 = channel 1
        event[1] = (byte) middle;  // eg:0x3C = middle C
        event[2] = (byte) 0x00;  // 0x00 = the minimum velocity (0)

        // Send the MIDI event to the synthesizer.
        midiDriver.write(event);

    }


    @Override
    public void onMidiStart() {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.d(this.getClass().getName(), "Motion event: " + event);

        if (v.getId() == R.id.c4) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x3C);  //sound note
                c4.setBackgroundColor(getResources().getColor(R.color.touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x3C);
                c4.setBackgroundColor(getResources().getColor(R.color.return_key));
            }
        }
        if (v.getId() == R.id.d4) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x3E);
                d4.setBackgroundColor(getResources().getColor(R.color.touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x3E);
                d4.setBackgroundColor(getResources().getColor(R.color.return_key));
            }
        }
        if (v.getId() == R.id.e4) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x40);
                e4.setBackgroundColor(getResources().getColor(R.color.touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x40);
                e4.setBackgroundColor(getResources().getColor(R.color.return_key));
            }
        }
        if (v.getId() == R.id.f4) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x41);
                f4.setBackgroundColor(getResources().getColor(R.color.touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x41);
                f4.setBackgroundColor(getResources().getColor(R.color.return_key));
            }
        }
        if (v.getId() == R.id.g4) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x43);
                g4.setBackgroundColor(getResources().getColor(R.color.touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x43);
                g4.setBackgroundColor(getResources().getColor(R.color.return_key));
            }
        }
        if (v.getId() == R.id.a4) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x45);
                a4.setBackgroundColor(getResources().getColor(R.color.touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x45);
                a4.setBackgroundColor(getResources().getColor(R.color.return_key));
            }
        }
        if (v.getId() == R.id.b4) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x47);
                b4.setBackgroundColor(getResources().getColor(R.color.touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x47);
                b4.setBackgroundColor(getResources().getColor(R.color.return_key));
            }
        }
        if (v.getId() == R.id.c5) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x48);
                c5.setBackgroundColor(getResources().getColor(R.color.touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x48);
                c5.setBackgroundColor(getResources().getColor(R.color.return_key));
            }
        }
        if (v.getId() == R.id.c4_b) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x3D);
                c4_b.setBackgroundColor(getResources().getColor(R.color.t_touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x3D);
                c4_b.setBackgroundColor(getResources().getColor(R.color.black));
            }
        }
        if (v.getId() == R.id.eb4_b) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x3F);
                eb4_b.setBackgroundColor(getResources().getColor(R.color.t_touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x3F);
                eb4_b.setBackgroundColor(getResources().getColor(R.color.black));
            }
        }
        if (v.getId() == R.id.f4_b) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x42);
                f4_b.setBackgroundColor(getResources().getColor(R.color.t_touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x42);
                f4_b.setBackgroundColor(getResources().getColor(R.color.black));
            }
        }
        if (v.getId() == R.id.g4_b) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x44);
                g4_b.setBackgroundColor(getResources().getColor(R.color.t_touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x44);
                g4_b.setBackgroundColor(getResources().getColor(R.color.black));
            }
        }
        if (v.getId() == R.id.bb4_b) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x46);
                bb4_b.setBackgroundColor(getResources().getColor(R.color.t_touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x46);
                bb4_b.setBackgroundColor(getResources().getColor(R.color.black));
            }
        }
        if (v.getId() == R.id.c5_b) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x49);
                c5_b.setBackgroundColor(getResources().getColor(R.color.t_touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x49);
                c5_b.setBackgroundColor(getResources().getColor(R.color.black));
            }
        }
        if (v.getId() == R.id.c5_c) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_DOWN");
                playNote(0x90,0x00,0x40);
                c5_c.setBackgroundColor(getResources().getColor(R.color.t_touched));
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(this.getClass().getName(), "MotionEvent.ACTION_UP");
                stopNote(0x80, 0x00, 0x40);
                c5_c.setBackgroundColor(getResources().getColor(R.color.black));
            }
        }
        return false;
    }


    @Override
    public void onClick(View v) {


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private class PianoView {
        public void scroll(Object i) {
        }
    }
}
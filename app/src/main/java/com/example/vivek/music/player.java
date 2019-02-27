package com.example.vivek.music;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class player extends AppCompatActivity {
    Button next,pre,pause;
    SeekBar bar;
    TextView songLabel;
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mysong;
    Thread updateseekbar;
    String sname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        //play=findViewById(R.id.play);
        next=findViewById(R.id.next);
        pre=findViewById(R.id.previous);
        bar=findViewById(R.id.seekbar);
        pause=findViewById(R.id.pause);
        songLabel=findViewById(R.id.label);

        updateseekbar=new Thread(){
            @Override
            public void run() {
                int duration=mediaPlayer.getDuration();
                int current=0;
                while(current<duration){
                    try{
                            sleep(500);
                            current=mediaPlayer.getCurrentPosition();
                            bar.setProgress(current);

                    }catch(InterruptedException e){
                        e.printStackTrace();

                    }
                }
            }
        };
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mysong=(ArrayList)bundle.getParcelableArrayList("songs");
        sname=mysong.get(position).getName().toString();
        String songname=i.getStringExtra("songname");
        songLabel.setText(songname);
        songLabel.setSelected(true);
        position=bundle.getInt("pos",0);
        Uri u= Uri.parse(mysong.get(position).toString());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        mediaPlayer.start();
        bar.setMax(mediaPlayer.getDuration());
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.setMax(mediaPlayer.getDuration());
                if(mediaPlayer.isPlaying()){
                    pause.setBackgroundResource(R.drawable.ic_play_arrow_black_24dp);
                    mediaPlayer.pause();
                }
                else{
                    pause.setBackgroundResource(R.drawable.ic_pause_black_24dp);
                    mediaPlayer.start();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position+1)%mysong.size());
                Uri u=Uri.parse(mysong.get(position).toString());
                mediaPlayer=mediaPlayer.create(getApplicationContext(),u);
                sname=mysong.get(position).getName().toString();
                songLabel.setText(sname);
                mediaPlayer.start();
            }
        });

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position=((position-1)<0)?(mysong.size()-1):(position-1);
                Uri u=Uri.parse(mysong.get(position).toString());
                mediaPlayer=mediaPlayer.create(getApplicationContext(),u);
                sname=mysong.get(position).getName().toString();
                songLabel.setText(sname);
                mediaPlayer.start();
            }
        });

    }
}

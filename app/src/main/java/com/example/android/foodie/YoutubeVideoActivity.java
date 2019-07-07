package com.example.android.foodie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView youTubePlayerView;
    private TextView textView;
    private TextView detailView;

    String name;
    String video;
    String videoCode;
    String details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video);

        youTubePlayerView = findViewById(R.id.youtube_player);
        textView =findViewById(R.id.name);
        detailView = findViewById(R.id.description);

        youTubePlayerView.initialize(Config.DEVELOPER_KEY, this);

        Intent intent = getIntent();
            name = intent.getStringExtra("name");
            video = intent.getStringExtra("url");
            details = intent.getStringExtra("details");

            textView.setText(name);
            detailView.setText(details);

            String [] seperator = video.split("=");
            String url = seperator[0];
            videoCode = seperator[1];
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

        if (!b){
            youTubePlayer.loadVideo(videoCode);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}

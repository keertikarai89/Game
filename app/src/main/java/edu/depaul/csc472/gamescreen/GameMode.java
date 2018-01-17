package edu.depaul.csc472.gamescreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.depaul.csc472.gamescreen.utility.Sound;

public class GameMode extends AppCompatActivity {

    private Sound soundHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        soundHelper = new Sound(this);
        soundHelper.prepareMusicPlayer(this);

        soundHelper.playMusic();

        final Button button_easy = findViewById(R.id.easy);

        button_easy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GameMode.this, how_to_play.class);
                startActivity(intent);

            }
        });

        final Button button_difficult = findViewById(R.id.difficult);

        button_difficult.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(GameMode.this, how_to_play_difficult.class);
                startActivity(intent);

            }
        });

    }
}

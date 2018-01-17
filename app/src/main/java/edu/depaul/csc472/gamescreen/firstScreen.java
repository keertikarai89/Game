package edu.depaul.csc472.gamescreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.depaul.csc472.gamescreen.utility.Sound;

public class firstScreen extends Activity {

    private Sound soundHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        soundHelper = new Sound(this);
        soundHelper.prepareMusicPlayer(this);

        soundHelper.playMusic();

        final Button button_play = findViewById(R.id.play);

        button_play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(firstScreen.this, GameMode.class);
                startActivity(intent);

            }
        });


    }


}

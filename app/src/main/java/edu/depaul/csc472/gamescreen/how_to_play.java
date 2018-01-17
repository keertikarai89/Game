package edu.depaul.csc472.gamescreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.depaul.csc472.gamescreen.utility.Sound;

public class how_to_play extends Activity {

    private Sound soundHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_play);

        soundHelper = new Sound(this);
        soundHelper.prepareMusicPlayer(this);

        soundHelper.playMusic();

        final Button button_continue = findViewById(R.id.button_continue);

        button_continue.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(how_to_play.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }
}

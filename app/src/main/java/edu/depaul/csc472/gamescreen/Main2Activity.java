package edu.depaul.csc472.gamescreen;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


import edu.depaul.csc472.gamescreen.utility.Score;
import edu.depaul.csc472.gamescreen.utility.Sound;

public class Main2Activity extends AppCompatActivity implements Balloon.BalloonListener {

    private static final String TAG = "Main2Activity";

    private static final int BALLOONS_PER_LEVEL = 60;
    private static final int NUMBER_OF_BALLOONS = 3;

    private static final int MIN_ANIMATION_DELAY = 500;
    private static final int MAX_ANIMATION_DELAY = 1000;
    private static final int MIN_ANIMATION_DURATION = 1000;
    private static final int MAX_ANIMATION_DURATION = 5000;
    private static final String ACTION_NEXT_LEVEL = "action_next_level";
    private static final String ACTION_RESTART_GAME = "action_restart_game";

    private ViewGroup ContentView;
    private Sound sound;
    private List<ImageView> balloonImages = new ArrayList<>();
    private List<Balloon> balloons = new ArrayList<>();
    private String nextAction = ACTION_RESTART_GAME;
    private boolean playing;
    private int[] balloonColors = new int[7];
    private int nextColor, balloonsPopped, screenWidth, screenHeight, balloonsUsed = 0, score = 0, level = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Toast.makeText(Main2Activity.this, "Click on Play button to start the game!", Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final Button button_play = findViewById(R.id.play);
        final TextView scoreDisplay = findViewById(R.id.score_display);
        final TextView levelDisplay = findViewById(R.id.level_display);
        //final Button button_menu = findViewById(R.id.menu);


//      Get background reference.
        ContentView = (ViewGroup) findViewById(R.id.content_view);
        if (ContentView == null) throw new AssertionError();
        ContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setToFullScreen();
                }
                return false;
            }
        });
        setToFullScreen();

//      Screen dimensions.
        ViewTreeObserver viewTreeObserver = ContentView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    screenWidth = ContentView.getWidth();
                    screenHeight = ContentView.getHeight();
                }
            });
        }

        sound = new Sound(this);
        sound.prepareMusicPlayer(this);

//      Initialize display elements
        balloonImages.add((ImageView) findViewById(R.id.balloon1));
        balloonImages.add((ImageView) findViewById(R.id.balloon2));
        balloonImages.add((ImageView) findViewById(R.id.balloon3));
//      Display current level and score
        updateDisplay();


//      Initialize balloon colors
        balloonColors[0] = Color.MAGENTA;
        balloonColors[1] = Color.CYAN;
        balloonColors[2] = Color.GREEN;
        balloonColors[3] = Color.RED;
        balloonColors[4] = Color.YELLOW;
        balloonColors[5] = Color.TRANSPARENT;
        balloonColors[6] = Color.BLACK;

//      Start Button
        if (button_play == null) throw new AssertionError();
        button_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (nextAction) {
                    case ACTION_RESTART_GAME:
                        startGame();
                        break;
                        case ACTION_NEXT_LEVEL:
                            startLevel();
                            break;
                    }
                }
            });

        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gameOver(false);
    }

    private void setToFullScreen() {

        //      Set full screen mode
        ContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void startGame() {
        final Button button_play = findViewById(R.id.play);
        setToFullScreen();

//      Reset score and level
        score = 0;
        level = 1;

//      Update display
        updateDisplay();

//      Reset Balloons
        balloonsUsed = 0;
        for (ImageView balloon : balloonImages) {
            balloon.setImageResource(R.drawable.balloon);
        }

//      Start the first level
        startLevel();
        sound.playMusic();
    }


    private void startLevel() {
        final Button button_play = findViewById(R.id.play);
//      Display the current level and score
        updateDisplay();

//      Reset flags for new level
        playing = true;
        balloonsPopped = 0;

//      integer arg for BalloonLauncher indicates the level
        Main2Activity.BalloonLauncher mLauncher = new Main2Activity.BalloonLauncher();
        mLauncher.execute(level);

    }

    private void finishLevel() {
        final Button button_play = findViewById(R.id.play);
        Score.setCurrentScore(this, score);
        Score.setCurrentLevel(this, level);
        Toast.makeText(Main2Activity.this,
                String.format(getString(R.string.you_finished_level_n), level),
                Toast.LENGTH_LONG).show();

        playing = false;
        level++;
        button_play.setText(String.format("Start level %s", level));
        nextAction = ACTION_NEXT_LEVEL;
    }

    private void updateDisplay() {
        final TextView scoreDisplay = findViewById(R.id.score_display);
        final TextView levelDisplay = findViewById(R.id.level_display);
        scoreDisplay.setText(String.valueOf(score));
        levelDisplay.setText(String.valueOf(level));
    }

    private void launchBalloon(int x) {

//      Balloon is launched from activity upon progress update from the AsyncTask
//      Create new imageview and set its tint color
        Balloon balloon = new Balloon(this, balloonColors[nextColor], 150, level);
        balloons.add(balloon);

//      Reset color for next balloon
        if (nextColor + 1 == balloonColors.length) {
            nextColor = 0;
        } else {
            nextColor++;
        }

//      Set balloon vertical position and dimensions, add to container
        balloon.setX(x);
        balloon.setY(screenHeight + balloon.getHeight());
        ContentView.addView(balloon);

        int duration = Math.max(MIN_ANIMATION_DURATION, MAX_ANIMATION_DURATION - (level * 1000));
        balloon.releaseBalloon(screenHeight, duration);

    }

    @Override
    public void popBalloon(Balloon balloon, boolean userTouch) {

//      Play sound, make balloon go away
        sound.playSound(balloon);
        ContentView.removeView(balloon);
        balloons.remove(balloon);
        balloonsPopped++;

        if (userTouch) {
            score= score+5;
        } else {
            balloonsUsed++;
            if (balloonsUsed <= balloonImages.size()) {
                balloonImages.get(balloonsUsed - 1)
                        .setImageResource(R.drawable.missed_balloon);
            }
            if (balloonsUsed == NUMBER_OF_BALLOONS) {
                gameOver(true);
                return;
            } //else {
            //Toast.makeText(Main2Activity.this,
            //      R.string.missed_balloon, Toast.LENGTH_SHORT).show();
            //}
        }
        updateDisplay();
        if (balloonsPopped == BALLOONS_PER_LEVEL) {
            finishLevel();
        }
    }

    private void gameOver(boolean allBalloonsUsed) {

        Toast.makeText(Main2Activity.this, R.string.game_over, Toast.LENGTH_LONG).show();
        //sound.stopMusic();

//      Clean up balloons
        for (Balloon balloon : balloons) {
            balloon.setPopped(true);
        }

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (Balloon balloon : balloons) {
                    ContentView.removeView(balloon);
                }
                balloons.clear();
            }
        }, 2000);

//      Reset for a new game
        final Button button_play = findViewById(R.id.play);
        playing = false;
        balloonsUsed = 0;
        button_play.setText(R.string.play_game);
        nextAction = ACTION_RESTART_GAME;


    }

    private class BalloonLauncher extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            if (params.length != 1) {
                throw new AssertionError(
                        "Expected 1 param for current level");
            }

            int level = params[0];

//          level 1 = max delay; each ensuing level reduces delay by 500 ms
//            min delay is 250 ms
            int maxDelay = Math.max(MIN_ANIMATION_DELAY, (MAX_ANIMATION_DELAY - ((level - 1) * 500)));
            int minDelay = maxDelay / 2;

            int balloonsLaunched = 0;
            while (playing && balloonsLaunched < BALLOONS_PER_LEVEL) {

//              Get a random horizontal position for the next balloon
                Random random = new Random(new Date().getTime());
                int xPosition = random.nextInt(screenWidth - 200);
                publishProgress(xPosition);
                balloonsLaunched++;

//              Wait a random number of milliseconds before looping
                int delay = random.nextInt(minDelay) + minDelay;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            launchBalloon(xPosition);
        }

    }
}



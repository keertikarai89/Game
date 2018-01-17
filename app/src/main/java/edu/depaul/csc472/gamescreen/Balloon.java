package edu.depaul.csc472.gamescreen;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import edu.depaul.csc472.gamescreen.utility.Pixel;

;

public class Balloon extends android.support.v7.widget.AppCompatImageView
        implements View.OnTouchListener,
        Animator.AnimatorListener,
        ValueAnimator.AnimatorUpdateListener {

    public static final String TAG = "Balloon";

    private BalloonListener listener;
    private ValueAnimator animator;
    private boolean popped;

    public Balloon(Context context) {
        super(context);
    }

    public Balloon(Context context, int color, int height, int level) {
        super(context);

        this.listener = (BalloonListener) context;

        this.setImageResource(R.drawable.balloon);
        this.setColorFilter(color);

        int width = height / 2;

//      Calc balloon height and width as dp
        int balloon_height = Pixel.pixelsToDp(height, context);
        int balloon_width = Pixel.pixelsToDp(width, context);
        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(balloon_width, balloon_height);
        setLayoutParams(params);

        setOnTouchListener(this);
    }

    public void releaseBalloon(int screenHeight, int duration) {
        animator = new ValueAnimator();
        animator.setDuration(duration);
        animator.setFloatValues(screenHeight, 0f);
        animator.setInterpolator(new LinearInterpolator());
        animator.setTarget(this);
        animator.addListener(this);
        animator.addUpdateListener(this);
        animator.start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if (!popped) {
            setY((Float) animation.getAnimatedValue());
        }
    }

    public interface BalloonListener {
        void popBalloon(Balloon balloon, boolean touched);
    }

    @Override
    public void onAnimationStart(Animator animation) {
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!popped) {
            listener.popBalloon(this, false);
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!popped && event.getAction() == MotionEvent.ACTION_DOWN) {
            listener.popBalloon(this, true);
            popped = true;
            animator.cancel();
        }
        return true;
    }

    public void setPopped(boolean popped) {
        this.popped = popped;
    }

}
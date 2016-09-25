package com.jakubpetriska.gameengine.showcase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.jakubpetriska.gameengine.api.android.GameEngineFragment;
import com.jakubpetriska.gameengine.api.external.ExternalMessenger;
import com.jakubpetriska.gameengine.showcase.engine.PlayerController;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jakub on 8. 5. 2015.
 */
public class EngineActivity extends FragmentActivity {

    private static final String KEY_SCORE = "SCORE";

    @InjectView(R.id.fps)
    TextView fpsDisplay;
    @InjectView(R.id.score)
    TextView scoreDisplay;

    private Handler mHandler = new Handler();
    private GameEngineFragment mGameEngineFragment;

    private int mScore;
    private Runnable mScoreRunnable = new Runnable() {
        @Override
        public void run() {
            scoreDisplay.setText(mScore + "");
        }
    };

    private float fps;
    private Runnable mFpsRunnable = new Runnable() {
        @Override
        public void run() {
            fpsDisplay.setText(fps + "");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engine);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            mGameEngineFragment = GameEngineFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.engine_container, mGameEngineFragment).commit();
        } else {
            mGameEngineFragment = (GameEngineFragment) getSupportFragmentManager().findFragmentById(R.id.engine_container);
            mScore = savedInstanceState.getInt(KEY_SCORE);
        }

        mGameEngineFragment.getMessenger().registerMessageReceiver(Float.class,
                new ExternalMessenger.MessageReceiver<Float>() {
                    @Override
                    public void onNewMessage(Float fps) {
                        // Expects only floats containing fps
                        EngineActivity.this.fps = fps;
                        mHandler.post(mFpsRunnable);
                    }
                });
        mGameEngineFragment.getMessenger().registerMessageReceiver(Integer.class,
                new ExternalMessenger.MessageReceiver<Integer>() {
                    @Override
                    public void onNewMessage(Integer scoreDiff) {
                        mScore += scoreDiff;
                        mHandler.post(mScoreRunnable);
                    }
                });
        mGameEngineFragment.getMessenger().registerMessageReceiver(String.class,
                new ExternalMessenger.MessageReceiver<String>() {
            @Override
            public void onNewMessage(String message) {
                if(PlayerController.FINAL_MESSAGE.equals(message)) {
                    // Game over
                    Intent intent = new Intent(EngineActivity.this, GameOverActivity.class);
                    intent.putExtra(GameOverActivity.EXTRA_SCORE, mScore);
                    startActivity(intent);

                    finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, mScore);
    }
}

package com.jakubpetriska.gameengine.sample.android.examples;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.jakubpetriska.gameengine.api.android.GameEngineFragment;
import com.jakubpetriska.gameengine.api.external.ExternalMessenger;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PerformanceTestActivity extends MovementControlsActivity {

    @InjectView(com.jakubpetriska.gameengine.sample.R.id.fps)
    TextView fpsDisplay;

    private Handler mHandler = new Handler();
    private GameEngineFragment mGameEngineFragment;

    @Override
    protected GameEngineFragment getGameEngineFragment() {
        return mGameEngineFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.jakubpetriska.gameengine.sample.R.layout.activity_performance_test);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            mGameEngineFragment = GameEngineFragment.newInstance("performance_test_scene");
            getSupportFragmentManager().beginTransaction().add(com.jakubpetriska.gameengine.sample.R.id.engine_container, mGameEngineFragment).commit();
        } else {
            mGameEngineFragment = (GameEngineFragment) getSupportFragmentManager().findFragmentById(com.jakubpetriska.gameengine.sample.R.id.engine_container);
        }

        mGameEngineFragment.getMessenger().registerMessageReceiver(Float.class,
                new ExternalMessenger.MessageReceiver<Float>() {
                    @Override
                    public void onNewMessage(final Float fps) {
                        // Expects only floats containing fps
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                fpsDisplay.setText(fps + "");
                            }
                        });
                    }
                });

        setupMovementControls();
    }
}

package com.jakubpetriska.gameengine.sample.android.examples;

import android.os.Bundle;

import com.jakubpetriska.gameengine.api.android.GameEngineFragment;

import butterknife.ButterKnife;

public class ExampleEnvironmentActivity extends MovementControlsActivity {

    private GameEngineFragment mGameEngineFragment;

    @Override
    protected GameEngineFragment getGameEngineFragment() {
        return mGameEngineFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.jakubpetriska.gameengine.sample.R.layout.activity_example_environment);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            mGameEngineFragment = GameEngineFragment.newInstance("example_environment_scene");
            getSupportFragmentManager().beginTransaction().add(com.jakubpetriska.gameengine.sample.R.id.engine_container, mGameEngineFragment).commit();
        } else {
            mGameEngineFragment = (GameEngineFragment) getSupportFragmentManager().findFragmentById(com.jakubpetriska.gameengine.sample.R.id.engine_container);
        }

        setupMovementControls();
    }
}

package com.jakubpetriska.gameengine.sample.android.examples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.jakubpetriska.gameengine.api.android.GameEngineFragment;
import com.jakubpetriska.gameengine.sample.R;
import com.jakubpetriska.gameengine.sample.engine.SceneSwitchingController;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SceneSwitchingActivity extends ActionBarActivity {

    private GameEngineFragment mEngineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_switching);
        ButterKnife.inject(this);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.engine_container);
        if (fragment != null) {
            mEngineFragment = (GameEngineFragment) fragment;
        } else {
            mEngineFragment = GameEngineFragment.newInstance("one_cube_scene");
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.engine_container, mEngineFragment)
                    .commit();
        }
    }

    @OnClick(R.id.switch_scene)
    public void switchScene() {
        mEngineFragment.getMessenger()
                .sendMessage(SceneSwitchingController.SWITCH_SCENES_MESSAGE);
    }
}

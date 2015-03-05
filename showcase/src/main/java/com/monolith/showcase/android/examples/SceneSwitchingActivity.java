package com.monolith.showcase.android.examples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;

import com.monolith.android.MonolithFragment;
import com.monolith.showcase.R;
import com.monolith.showcase.engine.SceneSwitchingController;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jakub Petriska on 23. 2. 2015.
 */
public class SceneSwitchingActivity extends ActionBarActivity {

    private MonolithFragment mEngineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_switching);
        ButterKnife.inject(this);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.engine_container);
        if (fragment != null) {
            mEngineFragment = (MonolithFragment) fragment;
        } else {
            mEngineFragment = MonolithFragment.newInstance("one_cube_scene");
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.engine_container, mEngineFragment)
                    .commit();
        }
    }

    @OnClick(R.id.switch_scene)
    public void switchScene() {
        mEngineFragment.getInputMessenger()
                .sendMessage(SceneSwitchingController.SWITCH_SCENES_MESSAGE);
    }
}

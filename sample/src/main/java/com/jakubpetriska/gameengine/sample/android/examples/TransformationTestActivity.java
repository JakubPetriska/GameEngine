package com.jakubpetriska.gameengine.sample.android.examples;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.jakubpetriska.gameengine.api.android.GameEngineFragment;
import com.jakubpetriska.gameengine.sample.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransformationTestActivity extends FragmentActivity {

    private GameEngineFragment mGameEngineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformation_test);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            mGameEngineFragment = GameEngineFragment.newInstance("transformation_test_scene");
            getSupportFragmentManager().beginTransaction().add(R.id.engine_container, mGameEngineFragment).commit();
        } else {
            mGameEngineFragment = (GameEngineFragment) getSupportFragmentManager().findFragmentById(R.id.engine_container);
        }
    }

    @OnClick(R.id.rotation_button_x)
    public void selectX() {
        mGameEngineFragment.getMessenger().sendMessage("x");
    }

    @OnClick(R.id.rotation_button_y)
    public void selectY() {
        mGameEngineFragment.getMessenger().sendMessage("y");
    }

    @OnClick(R.id.rotation_button_z)
    public void selectZ() {
        mGameEngineFragment.getMessenger().sendMessage("z");
    }
}

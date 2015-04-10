package com.monolith.showcase.android.examples;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.monolith.api.android.MonolithFragment;
import com.monolith.showcase.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jakub on 10. 4. 2015.
 */
public class TransformationTestActivity extends FragmentActivity {

    private MonolithFragment mMonolithFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transformation_test);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            mMonolithFragment = MonolithFragment.newInstance("transformation_test_scene");
            getSupportFragmentManager().beginTransaction().add(R.id.engine_container, mMonolithFragment).commit();
        } else {
            mMonolithFragment = (MonolithFragment) getSupportFragmentManager().findFragmentById(R.id.engine_container);
        }
    }

    @OnClick(R.id.rotation_button_x)
    public void selectX() {
        mMonolithFragment.getInputMessenger().sendMessage("x");
    }

    @OnClick(R.id.rotation_button_y)
    public void selectY() {
        mMonolithFragment.getInputMessenger().sendMessage("y");
    }

    @OnClick(R.id.rotation_button_z)
    public void selectZ() {
        mMonolithFragment.getInputMessenger().sendMessage("z");
    }
}

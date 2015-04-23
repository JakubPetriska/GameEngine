package com.monolith.showcase.android.examples;

import android.os.Bundle;

import com.monolith.api.android.MonolithFragment;
import com.monolith.showcase.R;

import butterknife.ButterKnife;

public class ExampleEnvironmentActivity extends MovementControlsActivity {

    private MonolithFragment mMonolithFragment;

    @Override
    protected MonolithFragment getMonolithFragment() {
        return mMonolithFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_environment);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            mMonolithFragment = MonolithFragment.newInstance("example_environment_scene");
            getSupportFragmentManager().beginTransaction().add(R.id.engine_container, mMonolithFragment).commit();
        } else {
            mMonolithFragment = (MonolithFragment) getSupportFragmentManager().findFragmentById(R.id.engine_container);
        }

        setupMovementControls();
    }
}

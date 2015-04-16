package com.monolith.showcase.android.examples;

import android.os.Bundle;
import android.widget.TextView;

import com.monolith.api.android.MonolithFragment;
import com.monolith.api.external.InputMessenger;
import com.monolith.showcase.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jakub on 16. 4. 2015.
 */
public class PerformanceTestActivity extends MovementControlsActivity {

    @InjectView(R.id.fps)
    TextView fps;

    private MonolithFragment mMonolithFragment;

    @Override
    protected MonolithFragment getMonolithFragment() {
        return mMonolithFragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_test);
        ButterKnife.inject(this);

        if (savedInstanceState == null) {
            mMonolithFragment = MonolithFragment.newInstance("example_environment_scene");
            getSupportFragmentManager().beginTransaction().add(R.id.engine_container, mMonolithFragment).commit();
        } else {
            mMonolithFragment = (MonolithFragment) getSupportFragmentManager().findFragmentById(R.id.engine_container);
        }

        mMonolithFragment.getInputMessenger().registerMessageReceiver(String.class,
                new InputMessenger.MessageReceiver<String>() {
                    @Override
                    public void onNewMessage(String message) {

                    }
                });

        setupMovementControls();
    }
}

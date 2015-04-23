package com.monolith.showcase.android.examples;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.monolith.api.android.MonolithFragment;
import com.monolith.api.external.InputMessenger;
import com.monolith.showcase.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PerformanceTestActivity extends MovementControlsActivity {

    @InjectView(R.id.fps)
    TextView fpsDisplay;

    private Handler mHandler = new Handler();
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
            mMonolithFragment = MonolithFragment.newInstance("performance_test_scene");
            getSupportFragmentManager().beginTransaction().add(R.id.engine_container, mMonolithFragment).commit();
        } else {
            mMonolithFragment = (MonolithFragment) getSupportFragmentManager().findFragmentById(R.id.engine_container);
        }

        mMonolithFragment.getInputMessenger().registerMessageReceiver(Float.class,
                new InputMessenger.MessageReceiver<Float>() {
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

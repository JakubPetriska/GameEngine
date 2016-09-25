package com.jakubpetriska.gameengine.sample.android.examples;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MenuTestActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.jakubpetriska.gameengine.sample.R.layout.activity_menu);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(com.jakubpetriska.gameengine.sample.R.id.fragment_container, new MenuFragment())
                    .commit();
        }
    }
}

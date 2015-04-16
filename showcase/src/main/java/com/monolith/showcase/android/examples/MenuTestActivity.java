package com.monolith.showcase.android.examples;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.monolith.showcase.R;

/**
 * Created by Jakub on 17. 4. 2015.
 */
public class MenuTestActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, new MenuFragment())
                    .commit();
        }
    }
}

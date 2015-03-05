package com.monolith.showcase.android;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.monolith.showcase.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jakub Petriska on 14. 2. 2015.
 */
public abstract class BaseActionBarActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    protected abstract int getLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);
    }
}

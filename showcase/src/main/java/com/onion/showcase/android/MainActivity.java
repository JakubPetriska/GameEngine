package com.onion.showcase.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.onion.R;
import com.onion.android.OnionEngineActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jakub Petriska on 13. 2. 2015.
 */
public class MainActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.list)
    ListView mList;

    private int[] mExamples = new int[]{
            R.string.example_basic_activity};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(mToolbar);

        String[] examplesTitles = new String[mExamples.length];
        for(int i = 0; i < mExamples.length; i++) {
            examplesTitles[i] = getResources().getString(mExamples[i]);
        }

        mList.setAdapter(new ArrayAdapter<>(this, R.layout.list_item_simple, examplesTitles));

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(mExamples[position]) {
                    case R.string.example_basic_activity:
                        startActivity(new Intent(MainActivity.this, OnionEngineActivity.class));
                        return;
                }
            }
        });
    }
}

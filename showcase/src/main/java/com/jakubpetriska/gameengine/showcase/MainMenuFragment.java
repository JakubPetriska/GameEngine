package com.jakubpetriska.gameengine.showcase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jakub on 30. 4. 2015.
 */
public class MainMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, null, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.play)
    public void play() {
        Intent intent = new Intent(getActivity(), EngineActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.high_scores)
    public void highscores() {
        ((MainActivity) getActivity()).replaceFragment(new HighScoresFragment());
    }
}

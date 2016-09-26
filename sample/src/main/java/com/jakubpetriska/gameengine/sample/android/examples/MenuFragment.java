package com.jakubpetriska.gameengine.sample.android.examples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakubpetriska.gameengine.api.android.GameEngineFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.jakubpetriska.gameengine.sample.R.layout.fragment_menu, null, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(com.jakubpetriska.gameengine.sample.R.id.play)
    public void play() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(com.jakubpetriska.gameengine.sample.R.id.fragment_container, GameEngineFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}

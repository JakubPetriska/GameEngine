package com.monolith.showcase.android.examples;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.monolith.api.android.MonolithFragment;
import com.monolith.showcase.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, null, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @OnClick(R.id.play)
    public void play() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, MonolithFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}

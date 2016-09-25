package com.monolith.game;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.monolith.game.db.DbContract;
import com.monolith.game.db.ScoresProvider;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jakub on 10. 5. 2015.
 */
public class HighScoresFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int TOP_RESULTS_COUNT = 10;

    @InjectView(R.id.high_scores_container)
    LinearLayout highScoresContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscores, null, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoaderManager().initLoader(R.id.loader_scores, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                ScoresProvider.getUri(ScoresProvider.BASE_PATH_SCORES),
                null, null, null,
                DbContract.Scores.COLUMN_NAME_SCORE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        highScoresContainer.removeAllViews();
        if (data.moveToFirst()) {
            int columnIndexScore = data.getColumnIndex(DbContract.Scores.COLUMN_NAME_SCORE);
            int resultCount = 0;
            do {
                ++resultCount;

                View row = View.inflate(getActivity(), R.layout.view_highscore, null);
                ((TextView) row.findViewById(R.id.ordinal))
                        .setText(resultCount + ".");
                ((TextView) row.findViewById(R.id.score))
                        .setText(data.getInt(columnIndexScore) + "");
                highScoresContainer.addView(row);
            } while (resultCount < TOP_RESULTS_COUNT && data.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}

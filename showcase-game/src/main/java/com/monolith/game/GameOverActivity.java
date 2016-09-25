package com.monolith.game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Jakub on 10. 5. 2015.
 */
public class GameOverActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<ScoresLoader.ScoreStatus> {

    public static final String EXTRA_SCORE = "SCORE";

    @InjectView(R.id.new_high_score)
    TextView newHighScore;
    @InjectView(R.id.new_score)
    TextView newScore;
    @InjectView(R.id.score_container)
    RelativeLayout scoreContainer;
    @InjectView(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        ButterKnife.inject(this);

        getSupportLoaderManager().initLoader(R.id.loader_save_score, null, this);
    }

    @Override
    public Loader<ScoresLoader.ScoreStatus> onCreateLoader(int id, Bundle args) {
        return new ScoresLoader(this, getIntent().getIntExtra(EXTRA_SCORE, 0));
    }

    @Override
    public void onLoadFinished(Loader<ScoresLoader.ScoreStatus> loader, ScoresLoader.ScoreStatus data) {
        progress.setVisibility(View.GONE);
        scoreContainer.setVisibility(View.VISIBLE);
        if(data.isNewHighScore) {
            newHighScore.setVisibility(View.VISIBLE);
        }
        newScore.setText(data.score + "");
    }

    @Override
    public void onLoaderReset(Loader<ScoresLoader.ScoreStatus> loader) {

    }

    @OnClick(R.id.play_again)
    public void playAgain() {
        Intent intent = new Intent(this, EngineActivity.class);
        startActivity(intent);
        finish();
    }
}

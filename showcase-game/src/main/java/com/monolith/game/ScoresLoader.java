package com.monolith.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.monolith.game.db.DbContract;
import com.monolith.game.db.ScoresProvider;

public class ScoresLoader extends AsyncTaskLoader<ScoresLoader.ScoreStatus> {

    private int score;

    public ScoresLoader(Context context, int score) {
        super(context);
        this.score = score;
    }

    @Override
    public ScoreStatus loadInBackground() {
        if(score == 0) {
            return new ScoreStatus(score, false);
        }

        Cursor higherScores = getContext().getContentResolver().query(
                ScoresProvider.getUri(ScoresProvider.BASE_PATH_SCORES),
                null,
                DbContract.Scores.COLUMN_NAME_SCORE + ">=" + score,
                null,
                DbContract.Scores.COLUMN_NAME_SCORE + " ASC");

        if (!higherScores.moveToFirst()
                || higherScores.getInt(higherScores.getColumnIndex(DbContract.Scores.COLUMN_NAME_SCORE)) != score) {
            ContentValues values = new ContentValues();
            values.put(DbContract.Scores.COLUMN_NAME_SCORE, score);

            getContext().getContentResolver().insert(
                    ScoresProvider.getUri(ScoresProvider.BASE_PATH_SCORES),
                    values);
        }
        return new ScoreStatus(score, higherScores.getCount() == 0);
    }

    public static class ScoreStatus {
        public final int score;
        public final boolean isNewHighScore;

        public ScoreStatus(int score, boolean isNewHighScore) {
            this.score = score;
            this.isNewHighScore = isNewHighScore;
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}

package com.jakubpetriska.gameengine.sample.android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jakubpetriska.gameengine.api.android.GameEngineActivity;
import com.jakubpetriska.gameengine.sample.android.examples.MenuTestActivity;
import com.jakubpetriska.gameengine.sample.android.examples.SceneSwitchingActivity;
import com.jakubpetriska.gameengine.sample.android.examples.TransformationTestActivity;
import com.jakubpetriska.gameengine.sample.android.examples.EngineAsFragmentActivity;
import com.jakubpetriska.gameengine.sample.android.examples.ExampleEnvironmentActivity;
import com.jakubpetriska.gameengine.sample.android.examples.PerformanceTestActivity;

import butterknife.InjectView;

/**
 * Main Activity containing list of examples.
 */
public class MainActivity extends BaseActionBarActivity {

    @InjectView(com.jakubpetriska.gameengine.sample.R.id.list)
    ListView mList;

    private int[] mExamples = new int[]{
            com.jakubpetriska.gameengine.sample.R.string.example_basic_activity,
            com.jakubpetriska.gameengine.sample.R.string.example_basic_fragment,
            com.jakubpetriska.gameengine.sample.R.string.example_scene_switching,
            com.jakubpetriska.gameengine.sample.R.string.example_imported_model,
            com.jakubpetriska.gameengine.sample.R.string.example_transformation_test,
            com.jakubpetriska.gameengine.sample.R.string.example_camera_rotation,
            com.jakubpetriska.gameengine.sample.R.string.example_environment,
            com.jakubpetriska.gameengine.sample.R.string.example_collisions,
            com.jakubpetriska.gameengine.sample.R.string.example_performance_test,
            com.jakubpetriska.gameengine.sample.R.string.example_menu
    };

    @Override
    protected int getLayout() {
        return com.jakubpetriska.gameengine.sample.R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] examplesTitles = new String[mExamples.length];
        for (int i = 0; i < mExamples.length; i++) {
            examplesTitles[i] = getResources().getString(mExamples[i]);
        }

        mList.setAdapter(new ArrayAdapter<>(this, com.jakubpetriska.gameengine.sample.R.layout.list_item_simple, examplesTitles));

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                switch (mExamples[position]) {
                    case com.jakubpetriska.gameengine.sample.R.string.example_basic_activity:
                        intent = new Intent(MainActivity.this, GameEngineActivity.class);
                        break;
                    case com.jakubpetriska.gameengine.sample.R.string.example_basic_fragment:
                        intent = new Intent(MainActivity.this, EngineAsFragmentActivity.class);
                        break;
                    case com.jakubpetriska.gameengine.sample.R.string.example_scene_switching:
                        intent = new Intent(MainActivity.this, SceneSwitchingActivity.class);
                        break;
                    case com.jakubpetriska.gameengine.sample.R.string.example_imported_model:
                        intent = new Intent(MainActivity.this, GameEngineActivity.class);
                        intent.putExtra(GameEngineActivity.EXTRA_DEFAULT_SCENE_NAME, "imported_model_scene");
                        break;
                    case com.jakubpetriska.gameengine.sample.R.string.example_transformation_test:
                        intent = new Intent(MainActivity.this, TransformationTestActivity.class);
                        break;
                    case com.jakubpetriska.gameengine.sample.R.string.example_camera_rotation:
                        intent = new Intent(MainActivity.this, GameEngineActivity.class);
                        intent.putExtra(GameEngineActivity.EXTRA_DEFAULT_SCENE_NAME, "camera_rotation_scene");
                        break;
                    case com.jakubpetriska.gameengine.sample.R.string.example_environment:
                        intent = new Intent(MainActivity.this, ExampleEnvironmentActivity.class);
                        break;
                    case com.jakubpetriska.gameengine.sample.R.string.example_collisions:
                        intent = new Intent(MainActivity.this, GameEngineActivity.class);
                        intent.putExtra(GameEngineActivity.EXTRA_DEFAULT_SCENE_NAME, "collisions_scene");
                        break;
                    case com.jakubpetriska.gameengine.sample.R.string.example_performance_test:
                        intent = new Intent(MainActivity.this, PerformanceTestActivity.class);
                        break;
                    case com.jakubpetriska.gameengine.sample.R.string.example_menu:
                        intent = new Intent(MainActivity.this, MenuTestActivity.class);
                        break;
                    default:
                        throw new IllegalStateException("Unknown option.");
                }
                startActivity(intent);
            }
        });
    }
}

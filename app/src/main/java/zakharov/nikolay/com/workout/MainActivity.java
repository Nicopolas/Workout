package zakharov.nikolay.com.workout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import zakharov.nikolay.com.workout.fragments.WorkoutDetailFragment;
import zakharov.nikolay.com.workout.fragments.WorkoutListFragment;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String WORKOUT_INDEX = "index";
    public static final String NAME_OF_FRAGMENT = "nameOfFragment";

    public FragmentManager fm = getSupportFragmentManager();
    public Fragment fragment = fm.findFragmentById(R.id.fragment_container);//создание фрагмента

    public int workoutIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null){
            workoutIndex = savedInstanceState.getInt(WORKOUT_INDEX);
            if (savedInstanceState.getString(NAME_OF_FRAGMENT).contains(WorkoutDetailFragment.TAG)) {
                startFragment(new WorkoutDetailFragment());
            }
        }

        if (fragment == null) {
            startFragment(new WorkoutListFragment());
        }

    }

    public void startFragment(Fragment nameFragment) {
        fragment = nameFragment;
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
        if (fragment.toString().contains(WorkoutDetailFragment.TAG)) {
            startFragment(new WorkoutListFragment());
            return;
        }
        super.onBackPressed();
    }


    //Добавление меню в action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_nemu, menu);
        return true;
    }

    //якоря логирования
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putString(NAME_OF_FRAGMENT, fragment.toString());
        savedInstanceState.putInt(WORKOUT_INDEX, workoutIndex);
    }//сохранение данных активности
}


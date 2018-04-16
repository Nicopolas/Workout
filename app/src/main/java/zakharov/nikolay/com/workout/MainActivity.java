package zakharov.nikolay.com.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import zakharov.nikolay.com.workout.model.WorkoutList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String WORKOUT_INDEX = "index";
    public static final int REQUEST_CODE = 100;
    Button workout_1;
    Button workout_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WorkoutList.getInstance(this);
        initGUI();
        setListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            workout_1.setText("Повторов: " + WorkoutList.getWorkouts().get(1).getRecordCount());//через синглтон
            if (data != null) {
                int repsCount = WorkoutDetailActivity.getRepsCountForResult(data);
                workout_2.setText("Повторов: " + repsCount);//через резулт
                WorkoutList.getWorkouts().get(2).setRecordCount(repsCount);
            }
        }
    }

    private void initGUI() {
        workout_1 = findViewById(R.id.workout_1);
        workout_2 = findViewById(R.id.workout_2);

        setWorkoutRecordsOnButton();
    }

    private void setWorkoutRecordsOnButton() {
        int firstWorkoutRecord = WorkoutList.getWorkouts().get(1).getRecordCount();
        int secondWorkoutRecord = WorkoutList.getWorkouts().get(2).getRecordCount();
        if (firstWorkoutRecord != 0) {
            workout_1.setText("Повторов: " + firstWorkoutRecord);
        }
        if (secondWorkoutRecord != 0) {
            workout_2.setText("Повторов: " + secondWorkoutRecord);
        }
    }

    private void setListeners() {
        workout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(MainActivity.this, WorkoutDetailActivity.class);
                detailIntent.putExtra(WORKOUT_INDEX, 1);
                startActivity(detailIntent);
            }
        });

        workout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(MainActivity.this, WorkoutDetailActivity.class);
                detailIntent.putExtra(WORKOUT_INDEX, 2);
                startActivityForResult(detailIntent, REQUEST_CODE);
            }
        });
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
        setWorkoutRecordsOnButton();
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
}

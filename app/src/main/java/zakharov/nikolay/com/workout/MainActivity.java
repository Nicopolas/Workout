package zakharov.nikolay.com.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import zakharov.nikolay.com.workout.model.WorkoutList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String WORKOUT_INDEX = "index";
    public static final int REQUEST_CODE = 100;

    ImageView workoutImageView;
    TextView workoutTitleTextView;
    TextView workoutItemTextView;
    TextView workoutRepsCount;
    TextView workoutLastRecordDate;

    CheckBox firstCheckBox;
    CheckBox secondCheckBox;
    CheckBox thirdCheckBox;

    LinearLayout workoutLayout;

    private RecyclerView workoutRecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* workoutRecyclerView = findViewById(R.id.workout_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        workoutRecyclerView.setLayoutManager(layoutManager);*/

/*        adapter = new MyAdapter(myDataset);
        workoutRecyclerView.setAdapter(mAdapter);*/

        WorkoutList.getInstance(this);
        initGUI();
        setListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            workoutRepsCount.setText("Повторов: " + WorkoutList.getWorkouts().get(1).getRecordCount());//через синглтон
            if (data != null) {
                int repsCount = WorkoutDetailActivity.getRepsCountForResult(data);
                workoutRepsCount.setText("Повторов: " + repsCount);//через резулт
                WorkoutList.getWorkouts().get(2).setRecordCount(repsCount);
                workoutLastRecordDate.setText(dataFormat(WorkoutList.getWorkouts().get(2).getRecordDate()));
            }
        }
    }

    private void initGUI() {
        workoutImageView = findViewById(R.id.workout_list_item_image_view);
        workoutTitleTextView = findViewById(R.id.workout_list_title_text_view);
        workoutItemTextView = findViewById(R.id.workout_list_description_text_view);
        workoutRepsCount = findViewById(R.id.reps_count);
        workoutLastRecordDate = findViewById(R.id.last_record_date);
        firstCheckBox = findViewById(R.id.firstCheckBox);
        secondCheckBox = findViewById(R.id.secondCheckBox);
        thirdCheckBox = findViewById(R.id.thirdCheckBox);

        workoutLayout = findViewById(R.id.workout_layout);

        setWorkoutRecordsOnButton();
    }

    private void setWorkoutRecordsOnButton() {
        int firstWorkoutRecord = WorkoutList.getWorkouts().get(1).getRecordCount();
        int secondWorkoutRecord = WorkoutList.getWorkouts().get(2).getRecordCount();
        if (firstWorkoutRecord != 0) {
            workoutRepsCount.setText("Повторов: " + firstWorkoutRecord);
        }
        if (secondWorkoutRecord != 0) {
            workoutRepsCount.setText("Повторов: " + secondWorkoutRecord);
        }
    }

    private void setListeners() {

        workoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = WorkoutDetailActivity.newIntent(MainActivity.this, firstCheckBox.isChecked(), secondCheckBox.isChecked(), thirdCheckBox.isChecked());
                detailIntent.putExtra(WORKOUT_INDEX, 2);
                startActivityForResult(detailIntent, REQUEST_CODE);
                firstCheckBox.isChecked();
            }
        });
    }

    private String dataFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy 'в' HH:mm:ss", new Locale("ru"));
        return dateFormat.format(date).toString();
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

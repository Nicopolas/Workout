package zakharov.nikolay.com.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import zakharov.nikolay.com.workout.model.Workout;
import zakharov.nikolay.com.workout.model.WorkoutList;

public class WorkoutDetailActivity extends AppCompatActivity {
    public static final String TAG = "WorkoutDetailActivity";
    private static final String EXTRA_REPS_COUNT =
            "zakharov.nikolay.com.workout.WorkoutDetailActivity.repsCount";
    private static final String EXTRA_LAST_RECORD_DATE =
            "zakharov.nikolay.com.workout.answer_shown";
    Button saveRecordButton;
    Button ShareButton;
    SeekBar repsSeekBar;
    TextView repsCountTextView;
    TextView recordDateTextView;
    TextView recordRepsCountTextView;
    TextView workoutTitleTextView;
    TextView workoutDescriptionTextView;
    ImageView pullUpsImageView;

    int repsCount = 0;
    int workoutIndex;
    String lastRecordDate;

    public static int getRepsCountForResult(Intent result) {
        return result.getIntExtra(EXTRA_REPS_COUNT, 0);
    }//методы для вызова переменных из результата

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        workoutIndex = getIntent().getIntExtra(MainActivity.WORKOUT_INDEX, 0);

        initGUI(workoutIndex);

        if (savedInstanceState != null) {
            repsCount = savedInstanceState.getInt(EXTRA_REPS_COUNT, 0);
            lastRecordDate = savedInstanceState.getString(EXTRA_LAST_RECORD_DATE);
            setDataRecord();
        }

        List<?> list = new ArrayList<>();

        repsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                repsCountTextView.setText(MessageFormat.format(getString(R.string.repeat_count_label), i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        saveRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDataRecord();
                setResult();
                WorkoutList.getWorkouts().get(workoutIndex).setRecordCount(repsCount);
            }
        });

        ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOut();
            }
        });
    }

    private void initGUI(int index) {
        Workout workout = WorkoutList.getWorkoutByIndex(index);

        saveRecordButton = findViewById(R.id.save_record_button);
        ShareButton = findViewById(R.id.btn_share);
        repsSeekBar = findViewById(R.id.reps_seek_bar);
        repsCountTextView = findViewById(R.id.reps_count_text_view);
        recordDateTextView = findViewById(R.id.record_date_text_view);
        recordDateTextView.setText(new SimpleDateFormat("dd.MM.yyy").format(workout.getRecordDate()));
        recordRepsCountTextView = findViewById(R.id.record_reps_text_view);
        recordRepsCountTextView.setText(String.valueOf(workout.getRecordCount()));
        workoutTitleTextView = findViewById(R.id.workout_title_lable);
        workoutTitleTextView.setText(workout.getTitle());
        workoutDescriptionTextView = findViewById(R.id.description_text_view);
    }

    public void sendOut() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, recordDateTextView.getText() + "\n" + recordRepsCountTextView.getText());
        startActivity(i);
    }//Поделиться

    private void setResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_REPS_COUNT, repsCount);//добавляем в интент repsCount
        setResult(RESULT_OK, data);
    } //заполнение интента результата

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(EXTRA_REPS_COUNT, repsCount);
        savedInstanceState.putString(EXTRA_LAST_RECORD_DATE, recordDateTextView.getText().toString());
    }//сохранение данных со странице в savedInstanceState

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
        if (repsCount != 0) {
            repsCount = WorkoutList.getWorkouts().get(workoutIndex).getRecordCount();
            lastRecordDate = String.valueOf(WorkoutList.getWorkouts().get(workoutIndex).getRecordDate());
        }
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


    private void setDataRecord() {
        int reps = Integer.parseInt(repsCountTextView.getText().toString().replaceAll("\\D", ""));
        if (recordRepsCountTextView.getText() != null && repsCount < reps) {
            repsCount = reps;
            lastRecordDate = dataFormat(new Date());
            recordRepsCountTextView.setText(
                    MessageFormat.format(WorkoutDetailActivity.this.getString(R.string.record_reps_label), repsCount));
            recordDateTextView.setText(
                    MessageFormat.format(WorkoutDetailActivity.this.getString(R.string.record_date_label), lastRecordDate));
            setResult();
        }
        recordRepsCountTextView.setText("Повторов: " + String.valueOf(repsCount));
        recordDateTextView.setText(
                MessageFormat.format(WorkoutDetailActivity.this.getString(R.string.record_date_label), lastRecordDate));
        setResult();
    }

    private String dataFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy 'в' HH:mm:ss", new Locale("ru"));
        return dateFormat.format(date).toString();
    }
}

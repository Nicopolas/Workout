package zakharov.nikolay.com.workout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String EXTRA_FIRST_CHECK_BOX_IS_CHECKED =
            "firstCheckBoxIsChecked";
    private static final String EXTRA_SECOND_CHECK_BOX_IS_CHECKED =
            "secondCheckBoxIsChecked";
    private static final String EXTRA_THIRD_CHECK_BOX_IS_CHECKED =
            "thirdCheckBoxIsChecked";
    Button saveRecordButton;
    Button ShareButton;
    SeekBar repsSeekBar;
    TextView repsCountTextView;
    TextView recordDateTextView;
    TextView recordRepsCountTextView;
    TextView workoutTitleTextView;
    TextView workoutDescriptionTextView;
    ImageView pullUpsImageView;
    TextView firstExtraField;
    TextView secondExtraField;
    TextView thirdExtraField;

    int repsCount = 0;
    int workoutIndex;
    String lastRecordDate;

    public static Intent newIntent(Context packageContext, int workoutIndex, boolean firstCheckBox, boolean secondCheckBox, boolean thirdCheckBox) {
        Intent intent = new Intent(packageContext, WorkoutDetailActivity.class);// создаем интент
        intent.putExtra(MainActivity.WORKOUT_INDEX, workoutIndex);
        intent.putExtra(EXTRA_FIRST_CHECK_BOX_IS_CHECKED, firstCheckBox);
        intent.putExtra(EXTRA_SECOND_CHECK_BOX_IS_CHECKED, secondCheckBox);
        intent.putExtra(EXTRA_THIRD_CHECK_BOX_IS_CHECKED, thirdCheckBox);
        return intent;
    }//статичный метод возвращающий интент (при создании)

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
        setListeners();
        checkCheckBoxMainActivity();

        if (savedInstanceState != null) {
            repsCount = savedInstanceState.getInt(EXTRA_REPS_COUNT, 0);
            lastRecordDate = savedInstanceState.getString(EXTRA_LAST_RECORD_DATE);
        }
        setDataRecord();

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


    private void initGUI(int index) {
        Workout workout = WorkoutList.getWorkoutByIndex(index);

        pullUpsImageView = findViewById(R.id.pull_ups_image_view);
        saveRecordButton = findViewById(R.id.save_record_button);
        ShareButton = findViewById(R.id.btn_share);
        repsSeekBar = findViewById(R.id.reps_seek_bar);
        repsCountTextView = findViewById(R.id.reps_count_text_view);
        recordDateTextView = findViewById(R.id.record_date_text_view);
        recordRepsCountTextView = findViewById(R.id.record_reps_text_view);
        workoutTitleTextView = findViewById(R.id.workout_title_lable);
        workoutTitleTextView.setText(workout.getTitle());
        workoutDescriptionTextView = findViewById(R.id.description_text_view);
        firstExtraField = findViewById(R.id.first_extra_field);
        secondExtraField = findViewById(R.id.second_extra_field);
        thirdExtraField = findViewById(R.id.third_extra_field);
    }

    private void setListeners() {
        saveRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDataRecord();
                setResult();
                WorkoutList.getWorkouts().get(workoutIndex).setRecordCount(repsCount);
                WorkoutList.getWorkouts().get(workoutIndex).setRecordDate(new Date());
            }
        });

        ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOut();
            }
        });
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
        if (repsCount != 0) {
            recordRepsCountTextView.setText("Повторов: " + String.valueOf(repsCount));
            recordDateTextView.setText(MessageFormat.format(WorkoutDetailActivity.this.getString(R.string.record_date_label), lastRecordDate));
            setResult();
        }
    }

    private String dataFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy 'в' HH:mm:ss", new Locale("ru"));
        return dateFormat.format(date).toString();
    }

    private void checkCheckBoxMainActivity() {
        setVisibilityElement(firstExtraField, getIntent().getBooleanExtra(EXTRA_FIRST_CHECK_BOX_IS_CHECKED, false));
        setVisibilityElement(secondExtraField, getIntent().getBooleanExtra(EXTRA_SECOND_CHECK_BOX_IS_CHECKED, false));
        setVisibilityElement(thirdExtraField, getIntent().getBooleanExtra(EXTRA_THIRD_CHECK_BOX_IS_CHECKED, false));
    }

    private void setVisibilityElement(View element, boolean isVisible) {
        if (isVisible) {
            element.setVisibility(View.VISIBLE);
            return;
        }
        element.setVisibility(View.GONE);
    }

    private void makeToast(String string) {
        Toast toast = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        toast.show();
    }
}

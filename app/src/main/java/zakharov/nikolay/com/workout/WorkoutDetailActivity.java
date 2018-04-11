package zakharov.nikolay.com.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
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

import android.net.Uri;
import android.widget.Toast;

import zakharov.nikolay.com.workout.model.Workout;
import zakharov.nikolay.com.workout.model.WorkoutList;

public class WorkoutDetailActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_detail);

        workoutIndex = getIntent().getIntExtra(MainActivity.WORKOUT_INDEX, 0);

        initGUI(workoutIndex);

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
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy", Locale.ROOT);
                int reps = Integer.parseInt(repsCountTextView.getText().toString().replaceAll("\\D", ""));
                if (recordRepsCountTextView.getText() != null && repsCount < reps) {
                    repsCount = reps;
                    recordRepsCountTextView.setText(
                            MessageFormat.format(WorkoutDetailActivity.this.getString(R.string.record_reps_label), repsCount));
                    recordDateTextView.setText(
                            MessageFormat.format(WorkoutDetailActivity.this.getString(R.string.record_date_label), sdf.format(new Date())));
                }
                //возврат результата c помощью интента
//                Intent resultIntent = getIntent();
//                resultIntent.putExtra("repeats", repsCount);
//                setResult(RESULT_OK, resultIntent);
//                finish();
                //возврат результата через модель
                WorkoutList.getWorkouts().get(workoutIndex).setRecordCount(repsCount);
            }
        });

        ShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                smsSend(recordDateTextView.getText().toString());
                //сюда неявный интент
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

    public void smsSend(String text) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, recordDateTextView.getText() + "\n" + recordRepsCountTextView.getText());
        i.putExtra(Intent.EXTRA_SUBJECT,
                "komuto");
        startActivity(i);
    }
}

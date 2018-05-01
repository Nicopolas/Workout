package zakharov.nikolay.com.workout.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import zakharov.nikolay.com.workout.MainActivity;
import zakharov.nikolay.com.workout.R;
import zakharov.nikolay.com.workout.model.Workout;
import zakharov.nikolay.com.workout.model.WorkoutList;

/**
 * Created by 1 on 30.04.2018.
 */

public class WorkoutDetailFragment extends Fragment {
    public static final String TAG = "WorkoutDetailFragment";
    public View view;
    private static final String EXTRA_REPS_COUNT =
            "zakharov.nikolay.com.workout.WorkoutDetailFragment.repsCount";
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
    TextView firstExtraField;
    TextView secondExtraField;
    TextView thirdExtraField;

    int workoutIndex;
    int repsCount;
    String lastRecordDate;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view = inflater.inflate(R.layout.workout_detail_fragment, container, false);
        this.view = view;

        WorkoutList.getInstance(getActivity());
        workoutIndex = ((MainActivity) getActivity()).workoutIndex;
        repsCount = WorkoutList.getWorkouts().get(workoutIndex).getRecordCount();

        initGUI(workoutIndex);
        setListeners();

        return view;
    }

    public void sendOut() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, recordDateTextView.getText() + "\n" + recordRepsCountTextView.getText());
        startActivity(i);
    }//Поделиться


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

        pullUpsImageView = view.findViewById(R.id.pull_ups_image_view);
        saveRecordButton = view.findViewById(R.id.save_record_button);
        ShareButton = view.findViewById(R.id.btn_share);
        repsSeekBar = view.findViewById(R.id.reps_seek_bar);
        repsCountTextView = view.findViewById(R.id.reps_count_text_view);
        recordDateTextView = view.findViewById(R.id.record_date_text_view);
        recordRepsCountTextView = view.findViewById(R.id.record_reps_text_view);
        workoutTitleTextView = view.findViewById(R.id.workout_title_lable);
        workoutTitleTextView.setText(workout.getTitle());
        workoutDescriptionTextView = view.findViewById(R.id.description_text_view);
        setDataRecord();
    }

    private void setListeners() {
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
                    MessageFormat.format(getActivity().getString(R.string.record_reps_label), repsCount));
            recordDateTextView.setText(
                    MessageFormat.format(getActivity().getString(R.string.record_date_label), lastRecordDate));
        }
        if (repsCount != 0) {
            recordRepsCountTextView.setText("Повторов: " + String.valueOf(repsCount));
            lastRecordDate = dataFormat(WorkoutList.getWorkouts().get(workoutIndex).getRecordDate());
            recordDateTextView.setText(MessageFormat.format(getActivity().getString(R.string.record_date_label), lastRecordDate));
        }
    }

    private String dataFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy 'в' HH:mm:ss", new Locale("ru"));
        return dateFormat.format(date).toString();
    }

    private void setVisibilityElement(View element, boolean isVisible) {
        if (isVisible) {
            element.setVisibility(View.VISIBLE);
            return;
        }
        element.setVisibility(View.GONE);
    }

    private void makeToast(String string) {
        Toast toast = Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT);
        toast.show();
    }

}

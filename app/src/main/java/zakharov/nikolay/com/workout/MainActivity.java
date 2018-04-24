package zakharov.nikolay.com.workout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import zakharov.nikolay.com.workout.model.Workout;
import zakharov.nikolay.com.workout.model.WorkoutList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private static final String EXTRA_FIRST_CHECK_BOX_IS_CHECKED =
            "firstCheckBoxIsChecked";
    private static final String EXTRA_SECOND_CHECK_BOX_IS_CHECKED =
            "secondCheckBoxIsChecked";
    private static final String EXTRA_THIRD_CHECK_BOX_IS_CHECKED =
            "thirdCheckBoxIsChecked";
    public static final String WORKOUT_INDEX = "index";

    RecyclerView workoutRecyclerView;
    WorkoutAdapter workoutAdapter;

    CheckBox firstCheckBox;
    CheckBox secondCheckBox;
    CheckBox thirdCheckBox;

    SharedPreferences sPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WorkoutList.getInstance(this);

        realizationOfRecyclerView();

        initGUI();
    }

    private void realizationOfRecyclerView() {
        workoutRecyclerView = findViewById(R.id.workout_recycler_view);
        workoutAdapter = new WorkoutAdapter(WorkoutList.getWorkouts(), this);
//      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        workoutRecyclerView.setAdapter(workoutAdapter);
        workoutRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initGUI() {
        firstCheckBox = findViewById(R.id.firstCheckBox);
        secondCheckBox = findViewById(R.id.secondCheckBox);
        thirdCheckBox = findViewById(R.id.thirdCheckBox);
    }

    private void setCheckBoxValueFromPreferences() {
        sPref = getPreferences(MODE_PRIVATE);
        firstCheckBox.setChecked(sPref.getBoolean(EXTRA_FIRST_CHECK_BOX_IS_CHECKED, false));
        secondCheckBox.setChecked(sPref.getBoolean(EXTRA_SECOND_CHECK_BOX_IS_CHECKED, false));
        thirdCheckBox.setChecked(sPref.getBoolean(EXTRA_THIRD_CHECK_BOX_IS_CHECKED, false));
    }

    private String dataFormat(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy '\nв' HH:mm:ss", new Locale("ru"));
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
        setCheckBoxValueFromPreferences();
        realizationOfRecyclerView();
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

        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(EXTRA_FIRST_CHECK_BOX_IS_CHECKED, firstCheckBox.isChecked());
        ed.putBoolean(EXTRA_SECOND_CHECK_BOX_IS_CHECKED, secondCheckBox.isChecked());
        ed.putBoolean(EXTRA_THIRD_CHECK_BOX_IS_CHECKED, thirdCheckBox.isChecked());
        ed.commit();
        Toast.makeText(this, "Text saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }


    static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        ImageView workoutImageView;
        TextView titleTextView;
        TextView descriptionTextView;
        TextView recordDateTextView;
        TextView recordRepsCountTextView;
        CardView itemCardView;
        ImageView popupMenuImageView;


        public WorkoutViewHolder(View itemView) {
            super(itemView);
            workoutImageView = itemView.findViewById(R.id.list_item_image_view);
            titleTextView = itemView.findViewById(R.id.list_item_title_text_view);
            descriptionTextView = itemView.findViewById(R.id.list_item_description_text_view);
            recordDateTextView = itemView.findViewById(R.id.list_item_records_date);
            recordRepsCountTextView = itemView.findViewById(R.id.list_item_record_repeats_count);
            itemCardView = itemView.findViewById(R.id.list_item_card_view);
            popupMenuImageView = itemView.findViewById(R.id.list_item_popup_menu);
        }
    }

    class WorkoutAdapter extends RecyclerView.Adapter<WorkoutViewHolder> {
        List<Workout> workouts;
        Context context;

        public WorkoutAdapter(List<Workout> workouts, Context context) {
            this.workouts = workouts;
            this.context = context;
        }

        @NonNull
        @Override
        public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_list_item,
                    parent, false);
            return new WorkoutViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final WorkoutViewHolder holder, int position) {
            Workout workout = workouts.get(holder.getAdapterPosition());
            holder.titleTextView.setText(workout.getTitle());
            holder.descriptionTextView.setText(workout.getDescription());
            holder.recordRepsCountTextView.setText(String.valueOf(workout.getRecordCount()));
            if (workout.getRecordCount() != 0) {
                holder.recordDateTextView.setText(dataFormat(workout.getRecordDate()));
            }
            holder.itemCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = WorkoutDetailActivity .newIntent(context, holder.getAdapterPosition(), firstCheckBox.isChecked(), secondCheckBox.isChecked(), thirdCheckBox.isChecked());
                    startActivity(intent);
                }
            });
            holder.popupMenuImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,
                            "Будет вызвано выпадающее меню для упражнение #" + holder.getAdapterPosition(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (workouts != null && workouts.size() != 0) {
                return workouts.size();
            }
            return 0;
        }
    }
}


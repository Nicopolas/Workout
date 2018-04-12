package zakharov.nikolay.com.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.Date;

import zakharov.nikolay.com.workout.model.WorkoutList;

public class MainActivity extends AppCompatActivity {

    public static final String WORKOUT_INDEX = "index";
    public static final int REQUEST_CODE = 100;
    Button workout_1;
    Button workout_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WorkoutList.getInstance(this);
        initGUI();
        setListeners();
        workout_2.setText("Повторов: " + WorkoutList.getWorkouts().get(2).getRecordCount());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (data != null) {
                workout_2.setText("Повторов: " + WorkoutDetailActivity.getRepsCountForResult(data));
            }
        }
    }

    private void initGUI() {
        workout_1 = findViewById(R.id.workout_1);
        workout_2 = findViewById(R.id.workout_2);
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
}

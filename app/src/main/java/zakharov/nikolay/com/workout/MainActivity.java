package zakharov.nikolay.com.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

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

        workout_1 = findViewById(R.id.workout_1);
        workout_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(MainActivity.this, WorkoutDetailActivity.class);
                detailIntent.putExtra(WORKOUT_INDEX, 1);
                startActivity(detailIntent);
            }
        });

        workout_2 = findViewById(R.id.workout_2);
        workout_2.setText("Повторов: " + WorkoutList.getWorkouts().get(2).getRecordCount());
        workout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(MainActivity.this, WorkoutDetailActivity.class);
                detailIntent.putExtra(WORKOUT_INDEX, 2);
                startActivityForResult(detailIntent, REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                workout_2.setText("Повторов: " + data.getIntExtra("repeats", 0));
            }
        }
    }
}

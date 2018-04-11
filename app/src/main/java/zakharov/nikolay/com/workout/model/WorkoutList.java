package  zakharov.nikolay.com.workout.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkoutList {
    private Context context;
    private static List<Workout> workouts;
    private static WorkoutList ourInstance;


    private WorkoutList(Context context) {
        this.context = context;
        workouts = initMockWorkouts();
    }

    public static WorkoutList getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new WorkoutList(context);
        }
        return ourInstance;
    }

    private List<Workout> initMockWorkouts() {
        List<Workout> workouts = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Workout workout = new Workout();
            workout.setTitle("Упражнение " + i);
            workout.setDescription("Описание упражнения " + i);
            workout.setRecordCount(i);
            workout.setRecordDate(new Date());
            workouts.add(workout);
        }
        return workouts;
    }

    public static List<Workout> getWorkouts() {
        return workouts;
    }

    public static Workout getWorkoutByIndex(int index) {
        return workouts.get(index);
    }
}

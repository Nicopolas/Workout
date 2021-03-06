package zakharov.nikolay.com.workout.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import zakharov.nikolay.com.workout.MainActivity;
import zakharov.nikolay.com.workout.R;
import zakharov.nikolay.com.workout.model.Workout;
import zakharov.nikolay.com.workout.model.WorkoutList;

/**
 * Created by 1 on 30.04.2018.
 */

public class WorkoutListFragment extends Fragment {
    public static final String TAG = "WorkoutListFragment";

    RecyclerView workoutRecyclerView;
    WorkoutAdapter workoutAdapter;
    public View view;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int flagg = WorkoutList.getInstance(getActivity()).flag+1;
        WorkoutList.getInstance(getActivity()).flag++;
        Log.d(TAG, "onCreateView called flag = " + flagg);
        view = inflater.inflate(R.layout.workout_list_fragment, container, false);
        setHasOptionsMenu(true);//важно для action bar

        WorkoutList.getInstance(getActivity());
        initGUI();

        return view;
    }

    //вывод PoPup меню
    private void showPopup(View v, final int index) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_open:
                        openDetailFragment(index);
                        return true;
                    case R.id.menu_delete:
                        WorkoutList.getWorkouts().remove(index);
                        initGUI();
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    private void openDetailFragment(int workoutIndex) {
        ((MainActivity) getActivity()).workoutIndex = workoutIndex;
        ((MainActivity) getActivity()).startFragment(new WorkoutDetailFragment());
    }

    private void realizationOfRecyclerView(View view) {
        workoutRecyclerView = view.findViewById(R.id.workout_recycler_view);
        workoutAdapter = new WorkoutAdapter(WorkoutList.getWorkouts(), getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        workoutRecyclerView.setAdapter(workoutAdapter);
        workoutRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void initGUI() {
        realizationOfRecyclerView(view);
    }


    //Добавление меню в action bar в фрагмент
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        return;
    }

    // обработка нажатий в action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                addElement();
                return true;
            case R.id.menu_clear:
                WorkoutList.getWorkouts().clear();
                initGUI(); ///спорно
                return true;
            case R.id.menu_exit_app:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addElement() {
        WorkoutList.getWorkouts().add(new Workout("Управжнение " + String.valueOf(WorkoutList.getWorkouts().size()),
                "Описание упражнения " + String.valueOf(WorkoutList.getWorkouts().size()),
                0, new Date()));
        initGUI();
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
        initGUI();
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
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                holder.titleTextView.setText(workout.getTitle());
                holder.descriptionTextView.setText(workout.getDescription());
            }
            holder.recordRepsCountTextView.setText(String.valueOf(workout.getRecordCount()));
            if (workout.getRecordCount() != 0) {
                holder.recordDateTextView.setText(dataFormat(workout.getRecordDate()));
            }
            holder.itemCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDetailFragment(holder.getAdapterPosition());
                }
            });
            holder.popupMenuImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopup(view, holder.getAdapterPosition());
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

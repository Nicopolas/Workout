package zakharov.nikolay.com.workout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import zakharov.nikolay.com.workout.fragments.WorkoutDetailFragment;
import zakharov.nikolay.com.workout.fragments.WorkoutListFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "MainActivity";
    public static final String WORKOUT_INDEX = "index";
    public static final String NAME_OF_FRAGMENT = "nameOfFragment";

    public FragmentManager fm = getSupportFragmentManager();
    public Fragment fragment = fm.findFragmentById(R.id.fragment_container);//создание фрагмента

    public int workoutIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate() called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_drawer_layout);

        if (savedInstanceState != null){
            workoutIndex = savedInstanceState.getInt(WORKOUT_INDEX);
            if (savedInstanceState.getString(NAME_OF_FRAGMENT).contains(WorkoutDetailFragment.TAG)) {
                startFragment(new WorkoutDetailFragment());
            }
        }

        if (fragment == null) {
            startFragment(new WorkoutListFragment());
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Находим drawer в ресурсах.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        // Этот класс связывает drawer и toolbar, позволяя drawer-у корректно отображаться с
        // hamburger menu в action bar, а также выдвигает drawer и задвигает.
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        // Добавляем слушателя на drawer.
        drawer.addDrawerListener(toggle);
        // Cинхронизируем hamburger menu с drawer.
        toggle.syncState();

        // Специальный класс для размещения пунктов списка в drawer'е
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Добавляем слушатель нажатий на пункт списка
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void startFragment(Fragment nameFragment) {
        fragment = nameFragment;
        fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }


    @Override
    public void onBackPressed() {
        //обработка нажатия в детальном фрагменте
        if (fragment.toString().contains(WorkoutDetailFragment.TAG)) {
            startFragment(new WorkoutListFragment());
            return;
        }
        //обработка нажатия в Navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
        super.onBackPressed();
    }

    //Добавление меню в action bar в активность
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_nemu, menu);
        return true;
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putString(NAME_OF_FRAGMENT, fragment.toString());
        savedInstanceState.putInt(WORKOUT_INDEX, workoutIndex);
    }//сохранение данных активности

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_main_windows:
                startFragment(new WorkoutListFragment());
                break;
            case R.id.nav_manage:
                makeToast("В разарботке");
                break;
            case R.id.nav_about_developers:
                makeToast("В разарботке");
                break;
            case R.id.nav_share:
                sendOut();
            case R.id.nav_exit_app:
                finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        // закрываем NavigationView, параметр определяет анимацию закрытия
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void makeToast(String string) {
        Toast toast = Toast.makeText(this, string, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void sendOut() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, "text");
        startActivity(i);
    }//Поделиться
}


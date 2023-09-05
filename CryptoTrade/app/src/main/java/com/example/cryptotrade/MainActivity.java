package com.example.cryptotrade;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeTransform;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView txtCoinName;
    private EditText searchBar;
    private RecyclerView cryptoRV;
    private ProgressBar progressBar;
    private ArrayList<CryptoModel> cryptoModelArrayList;
    private CryptoRVAdapter cryptoRVAdapter;
    private ImageView coinImg;
    ChipNavigationBar chipNavigationBar;
    int previouslySelectedItemId=-1;
    private Slide enterTransitionLeft = new Slide(Gravity.LEFT);
    private Slide enterTransitionRight = new Slide(Gravity.RIGHT);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        setUpNotification();

        chipNavigationBar = findViewById(R.id.bottom_nav_bar);
        getSupportFragmentManager().beginTransaction().replace(R.id.rel_layout, new HomeFragment()).commit();
        bottomMenu();

        enterTransitionLeft.setDuration(800);
        enterTransitionRight.setDuration(800);


    }


    private void bottomMenu(){
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment f = null;
                int previousItemId = previouslySelectedItemId;
                if (i == R.id.bottom_nav_home) {
                    f = new HomeFragment();
                    if(previouslySelectedItemId==R.id.bottom_nav_profile || previouslySelectedItemId==R.id.bottom_nav_trending){
                        f.setEnterTransition(enterTransitionRight);
                    } else if (previouslySelectedItemId == R.id.bottom_nav_trade) {
                        f.setEnterTransition(enterTransitionLeft);
                    }
                    previouslySelectedItemId = R.id.bottom_nav_home;
                } else if (i == R.id.bottom_nav_profile) {
                    f = new ProfileFragment();
                    if(previouslySelectedItemId==R.id.bottom_nav_trending || previouslySelectedItemId==R.id.bottom_nav_home || previouslySelectedItemId==R.id.bottom_nav_trade){
                        f.setEnterTransition(enterTransitionLeft);
                    }
                    previouslySelectedItemId = R.id.bottom_nav_profile;
                } else if (i ==R.id.bottom_nav_trending) {
                    f= new TrendingFragment();
                    if(previouslySelectedItemId==R.id.bottom_nav_profile){
                        f.setEnterTransition(enterTransitionRight);
                    } else if (previouslySelectedItemId==R.id.bottom_nav_home || previouslySelectedItemId==R.id.bottom_nav_trade){
                        f.setEnterTransition(enterTransitionLeft);
                    }
                    previouslySelectedItemId = R.id.bottom_nav_trending;
                } else if (i == R.id.bottom_nav_trade) {
                    f = new ExchangeFragment();
                    if(previouslySelectedItemId==R.id.bottom_nav_profile || previouslySelectedItemId==R.id.bottom_nav_trending || previouslySelectedItemId==R.id.bottom_nav_home){
                        f.setEnterTransition(enterTransitionRight);
                    }
                    previouslySelectedItemId = R.id.bottom_nav_trade;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.rel_layout, f).commit();
            }
        });
    }

    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String name = "TrendingNotifChannel";
            String description = "Channel for the trending notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("trendingnotif", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setUpNotification(){
        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_MUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        long time = System.currentTimeMillis();
        long tenSecondsInMillis = 1000 * 10;
        alarmManager.set(AlarmManager.RTC_WAKEUP, time+tenSecondsInMillis, pendingIntent);
    }
}
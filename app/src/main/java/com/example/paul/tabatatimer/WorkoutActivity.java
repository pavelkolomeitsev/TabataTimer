package com.example.paul.tabatatimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WorkoutActivity extends AppCompatActivity {

    public static final String SETTINGS = "settings";
    public static final String KEY_MINUTES = "MINUTES";
    public static final String KEY_SECONDS = "SECONDS";

    public static final String WORKOUT_MINUTES = "workout_minutes";
    public static final String WORKOUT_MINUTES_INTEGER = "workout_minutes_integer";
    public static final String WORKOUT_SECONDS = "workout_seconds";
    public static final String WORKOUT_SECONDS_INTEGER = "workout_seconds_integer";

    SharedPreferences settings;

    private static int minutes, seconds;

    TextView tvWorkoutMinutesExactly, tvWorkoutSecondsExactly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);

        initialization();

        loadSettings();
    }

    private void loadSettings() {

        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        String workoutMinutes = settings.getString(WORKOUT_MINUTES, "0 minutes");
        tvWorkoutMinutesExactly.setText(workoutMinutes);
        minutes = settings.getInt(WORKOUT_MINUTES_INTEGER, 0);

        String workoutSeconds = settings.getString(WORKOUT_SECONDS, "0 seconds");
        tvWorkoutSecondsExactly.setText(workoutSeconds);
        seconds = settings.getInt(WORKOUT_SECONDS_INTEGER, 0);
    }

    private void initialization() {

        minutes = 0;
        seconds = 0;
        tvWorkoutMinutesExactly = findViewById(R.id.tv_workout_minutes_exactly);
        tvWorkoutSecondsExactly = findViewById(R.id.tv_workout_seconds_exactly);
    }

    public void onClick(View view){

        switch (view.getId()){
            case R.id.btn_minus_minutes:
                --minutes;
                tvWorkoutMinutesExactly.setText(String.valueOf(minutes) + " minutes");
                break;
            case R.id.btn_plus_minutes:
                ++minutes;
                tvWorkoutMinutesExactly.setText(String.valueOf(minutes) + " minutes");
                break;
            case R.id.btn_minus_seconds:
                --seconds;
                tvWorkoutSecondsExactly.setText(String.valueOf(seconds) + " seconds");
                break;
            case R.id.btn_plus_seconds:
                ++seconds;
                tvWorkoutSecondsExactly.setText(String.valueOf(seconds) + " seconds");
                break;
            case R.id.btn_workout_save_time:
                if ((minutes < 0) || (minutes > 60)){
                    Toast.makeText(this, "The number of minutes is incorrect!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ((seconds < 0) || (seconds > 60)){
                    Toast.makeText(this, "The number of seconds is incorrect!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(KEY_MINUTES, minutes);
                intent.putExtra(KEY_SECONDS, seconds);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }

    }

    private void saveSettings() {

        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(WORKOUT_MINUTES, tvWorkoutMinutesExactly.getText().toString());
        editor.putInt(WORKOUT_MINUTES_INTEGER, minutes);
        editor.putString(WORKOUT_SECONDS, tvWorkoutSecondsExactly.getText().toString());
        editor.putInt(WORKOUT_SECONDS_INTEGER, seconds);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
    }
}

package com.example.paul.tabatatimer;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static int amountOfRounds;
    public static int secondsPrepareTime;
    public static int minutesWorkout;
    public static int secondsWorkout;
    public static int minutesBreak;
    public static int secondsBreak;

    public static final int REQUEST_CODE_WORKOUT = 1;
    public static final int REQUEST_CODE_BREAK = 2;

    public static final String SETTINGS = "settings";
    public static final String KEY_MINUTES = "MINUTES";
    public static final String KEY_SECONDS = "SECONDS";
    public static final String PREPARE_TIME = "prepare_time";
    public static final String PREPARE_TIME_INTEGER = "prepare_time_integer";
    public static final String WORKOUT_MINUTES = "workout_minutes";
    public static final String WORKOUT_MINUTES_INTEGER = "workout_minutes_integer";
    public static final String WORKOUT_SECONDS = "workout_seconds";
    public static final String WORKOUT_SECONDS_INTEGER = "workout_seconds_integer";
    public static final String BREAK_MINUTES = "break_minutes";
    public static final String BREAK_MINUTES_INTEGER = "break_minutes_integer";
    public static final String BREAK_SECONDS = "break_seconds";
    public static final String BREAK_SECONDS_INTEGER = "break_seconds_integer";
    public static final String ROUNDS = "rounds";
    public static final String ROUNDS_INTEGER = "rounds_integer";


    TextView tvRounds, tvPrepareTime, tvWorkoutMinutes, tvWorkoutSeconds, tvBreakMinutes, tvBreakSeconds;

    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        loadSettings();
    }

    private void loadSettings() {

        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        String prepareTime = settings.getString(PREPARE_TIME, "0 seconds");
        tvPrepareTime.setText(prepareTime);
        secondsPrepareTime = settings.getInt(PREPARE_TIME_INTEGER, 0);

        String workoutMinutes = settings.getString(WORKOUT_MINUTES, "0 min");
        tvWorkoutMinutes.setText(workoutMinutes);
        minutesWorkout = settings.getInt(WORKOUT_MINUTES_INTEGER, 0);

        String workoutSeconds = settings.getString(WORKOUT_SECONDS, "0 sec");
        tvWorkoutSeconds.setText(workoutSeconds);
        secondsWorkout = settings.getInt(WORKOUT_SECONDS_INTEGER, 0);

        String breakMinutes = settings.getString(BREAK_MINUTES, "0 min");
        tvBreakMinutes.setText(breakMinutes);
        minutesBreak = settings.getInt(BREAK_MINUTES_INTEGER, 0);

        String breakSeconds = settings.getString(BREAK_SECONDS, "0 sec");
        tvBreakSeconds.setText(breakSeconds);
        secondsBreak = settings.getInt(BREAK_SECONDS_INTEGER, 0);

        String rounds = settings.getString(ROUNDS, "0 rounds");
        tvRounds.setText(rounds);
        amountOfRounds = settings.getInt(ROUNDS_INTEGER, 0);
    }

    public void onClick(View view){
        Intent intent;

        switch (view.getId()){
            case R.id.btn_prepared:
                showDialogPrepareTime();
                break;
            case R.id.btn_rounds:
                showDialogRounds();
                break;
            case R.id.btn_workout:
                intent = new Intent(this, WorkoutActivity.class);
                startActivityForResult(intent, REQUEST_CODE_WORKOUT);
                break;
            case R.id.btn_break:
                intent = new Intent(this, BreakActivity.class);
                startActivityForResult(intent, REQUEST_CODE_BREAK);
                break;
            case R.id.btn_start:
                Bundle extras = new Bundle();
                extras.putInt("prepareTime", secondsPrepareTime);
                extras.putInt("workoutTime", (minutesWorkout * 60 + secondsWorkout));
                extras.putInt("breakTime", (minutesBreak * 60 + secondsBreak));
                extras.putInt("rounds", amountOfRounds);
                intent = new Intent(this, StartActivity.class); // create intent
                intent.putExtras(extras); // attach bundle
                startActivity(intent);
                break;
        }
    }

    private void showDialogPrepareTime() {
        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_prepare_time);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_prepare);
        Button btnSave = dialog.findViewById(R.id.btn_save_prepare);
        final NumberPicker numberPicker = dialog.findViewById(R.id.nb_prepare_time);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(60);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondsPrepareTime = numberPicker.getValue();
                tvPrepareTime.setText(String.valueOf(secondsPrepareTime) + " seconds");
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDialogRounds() {

        final Dialog dialog = new Dialog(this, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_rounds);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel_rounds);
        Button btnSave = dialog.findViewById(R.id.btn_save_rounds);
        final NumberPicker numberPicker = dialog.findViewById(R.id.nb_rounds);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountOfRounds = numberPicker.getValue();
                tvRounds.setText(String.valueOf(amountOfRounds) + " rounds");
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void initialization() {
        amountOfRounds = 0;
        secondsPrepareTime = 0;
        minutesWorkout = 0;
        secondsWorkout = 0;
        minutesBreak = 0;
        secondsBreak = 0;

        tvPrepareTime = findViewById(R.id.tv_prepare_time);
        tvWorkoutMinutes = findViewById(R.id.tv_workout_minutes);
        tvWorkoutSeconds = findViewById(R.id.tv_workout_seconds);
        tvBreakMinutes = findViewById(R.id.tv_break_minutes);
        tvBreakSeconds = findViewById(R.id.tv_break_seconds);
        tvRounds = findViewById(R.id.tv_rounds);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_CODE_WORKOUT:
                    minutesWorkout = data.getIntExtra(KEY_MINUTES, 0);
                    tvWorkoutMinutes.setText(String.valueOf(minutesWorkout) + " min");
                    secondsWorkout = data.getIntExtra(KEY_SECONDS, 0);
                    tvWorkoutSeconds.setText(String.valueOf(secondsWorkout) + " sec");
                    break;
                case REQUEST_CODE_BREAK:
                    minutesBreak = data.getIntExtra(KEY_MINUTES, 0);
                    tvBreakMinutes.setText(String.valueOf(minutesBreak) + " min");
                    secondsBreak = data.getIntExtra(KEY_SECONDS, 0);
                    tvBreakSeconds.setText(String.valueOf(secondsBreak) + " sec");
                    break;
            }
        }
    }

    private void saveSettings() {

        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(PREPARE_TIME, tvPrepareTime.getText().toString());
        editor.putInt(PREPARE_TIME_INTEGER, secondsPrepareTime);

        editor.putString(WORKOUT_MINUTES, tvWorkoutMinutes.getText().toString());
        editor.putInt(WORKOUT_MINUTES_INTEGER, minutesWorkout);

        editor.putString(WORKOUT_SECONDS, tvWorkoutSeconds.getText().toString());
        editor.putInt(WORKOUT_SECONDS_INTEGER, secondsWorkout);

        editor.putString(BREAK_MINUTES, tvBreakMinutes.getText().toString());
        editor.putInt(BREAK_MINUTES_INTEGER, minutesBreak);

        editor.putString(BREAK_SECONDS, tvBreakSeconds.getText().toString());
        editor.putInt(BREAK_SECONDS_INTEGER, secondsBreak);

        editor.putString(ROUNDS, tvRounds.getText().toString());
        editor.putInt(ROUNDS_INTEGER, amountOfRounds);

        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
    }
}

package com.example.paul.tabatatimer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class BreakActivity extends AppCompatActivity {

    public static final String SETTINGS = "settings";
    public static final String KEY_MINUTES = "MINUTES";
    public static final String KEY_SECONDS = "SECONDS";

    public static final String BREAK_MINUTES = "break_minutes";
    public static final String BREAK_MINUTES_INTEGER = "break_minutes_integer";
    public static final String BREAK_SECONDS = "break_seconds";
    public static final String BREAK_SECONDS_INTEGER = "break_seconds_integer";

    SharedPreferences settings;

    private static int minutes, seconds;

    TextView tvBreakMinutesExactly, tvBreakSecondsExactly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break);

        initialization();

        loadSettings();
    }

    private void loadSettings() {

        settings = getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        String breakMinutes = settings.getString(BREAK_MINUTES, "0 minutes");
        tvBreakMinutesExactly.setText(breakMinutes);
        minutes = settings.getInt(BREAK_MINUTES_INTEGER, 0);

        String breakSeconds = settings.getString(BREAK_SECONDS, "0 seconds");
        tvBreakSecondsExactly.setText(breakSeconds);
        seconds = settings.getInt(BREAK_SECONDS_INTEGER, 0);
    }

    private void initialization() {

        minutes = 0;
        seconds = 0;
        tvBreakMinutesExactly = findViewById(R.id.tv_break_minutes_exactly);
        tvBreakSecondsExactly = findViewById(R.id.tv_break_seconds_exactly);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_minus_minutes:
                --minutes;
                tvBreakMinutesExactly.setText(String.valueOf(minutes) + " minutes");
                break;
            case R.id.btn_plus_minutes:
                ++minutes;
                tvBreakMinutesExactly.setText(String.valueOf(minutes) + " minutes");
                break;
            case R.id.btn_minus_seconds:
                --seconds;
                tvBreakSecondsExactly.setText(String.valueOf(seconds) + " seconds");
                break;
            case R.id.btn_plus_seconds:
                ++seconds;
                tvBreakSecondsExactly.setText(String.valueOf(seconds) + " seconds");
                break;
            case R.id.btn_break_save_time:
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

        editor.putString(BREAK_MINUTES, tvBreakMinutesExactly.getText().toString());
        editor.putInt(BREAK_MINUTES_INTEGER, minutes);
        editor.putString(BREAK_SECONDS, tvBreakSecondsExactly.getText().toString());
        editor.putInt(BREAK_SECONDS_INTEGER, seconds);
        editor.apply();
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSettings();
    }
}

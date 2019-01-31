package com.example.paul.tabatatimer;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class StartActivity extends AppCompatActivity {

    private static long pauseChronometer;
    private static int prepareTime;
    private static int workoutTime;
    private static int breakTime;
    private static int rounds;
    private static int count;
    private static int countDownTime;
    private static boolean isRunning, isWorkout, isBreak;

    TextView tvCountdown, tvRounds;
    Button btnStopResume;
    Chronometer chronometer;
    Timer timer;

    private SoundPool soundPool;
    private SoundPool soundPoolExercises;
    private int exercises[];
    private int countDown, gong, ovation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        initialization();

        btnStopResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning){
                    cancelTimer();
                }else {
                    resumeTimer();
                }
            }
        });

        startTabata();
    }

    private void startTabata() {

        tvRounds.setText(count + "/" + rounds);

        isRunning = true;

        chronometer.setBase(SystemClock.elapsedRealtime() - pauseChronometer);
        chronometer.start();

        timer = new Timer();
        timer.execute();
    }

    private void resumeTimer() {
        btnStopResume.setText("STOP");
        startTabata();
    }

    private void cancelTimer() {
        btnStopResume.setText("RESUME");
        chronometer.stop();
        pauseChronometer = SystemClock.elapsedRealtime() - chronometer.getBase();
        isRunning = false;
        if (timer == null) return;
        timer.cancel(false);
    }

    private void initialization() {

        Intent intent = getIntent();

        prepareTime = intent.getIntExtra("prepareTime", 0);
        workoutTime = intent.getIntExtra("workoutTime", 0);
        breakTime = intent.getIntExtra("breakTime", 0);
        rounds = intent.getIntExtra("rounds", 0);

        count = 0;

        countDownTime = prepareTime + workoutTime * rounds + breakTime * rounds - breakTime;

        tvCountdown = findViewById(R.id.tvCountdown);
        tvRounds = findViewById(R.id.tv_rounds);
        chronometer = findViewById(R.id.chronomet);
        btnStopResume = findViewById(R.id.btn_stop_resume);

        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);

        countDown = soundPool.load(this, R.raw.go, 1);
        gong = soundPool.load(this, R.raw.gong, 1);
        ovation = soundPool.load(this, R.raw.ovations, 1);

        exercises = new int[10];
        soundPoolExercises = new SoundPool(exercises.length, AudioManager.STREAM_MUSIC,0);
        exercises[0] = soundPoolExercises.load(this, R.raw.exercise_1, 1);
        exercises[1] = soundPoolExercises.load(this, R.raw.exercise_2, 1);
        exercises[2] = soundPoolExercises.load(this, R.raw.exercise_3, 1);
        exercises[3] = soundPoolExercises.load(this, R.raw.exercise_4, 1);
        exercises[4] = soundPoolExercises.load(this, R.raw.exercise_5, 1);
        exercises[5] = soundPoolExercises.load(this, R.raw.exercise_6, 1);
        exercises[6] = soundPoolExercises.load(this, R.raw.exercise_7, 1);
        exercises[7] = soundPoolExercises.load(this, R.raw.exercise_8, 1);
        exercises[8] = soundPoolExercises.load(this, R.raw.exercise_9, 1);
        exercises[9] = soundPoolExercises.load(this, R.raw.exercise_10, 1);
    }

    class Timer extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            if(isCancelled()) return null;

            while (true){
                if (isCancelled()){
                    return null;
                }

                publishProgress(countDownTime);

                if (countDownTime == 0) break;

                countDownTime--;

                try {
                    TimeUnit.MILLISECONDS.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... time) {
            super.onProgressUpdate(time);

            int temp = time[0] - (workoutTime * rounds + breakTime * rounds - breakTime);

            if (temp == 3){
                soundPool.play(countDown, 1, 1, 0, 0, 1);
            }

            if (temp > 0){
                tvCountdown.setBackgroundResource(R.drawable.prepare_text_view);
                updateTimer(time[0] - (workoutTime * rounds + breakTime * rounds - breakTime));
            }else {
                if (time[0] % (workoutTime + breakTime) == workoutTime){
                    tvCountdown.setBackgroundResource(R.drawable.workout_text_view);
                    isWorkout = true;
                    isBreak = false;
                    soundPoolExercises.play(exercises[count], 1, 1, 0, 0, 1);
                    count++;
                }
                else if (time[0] % (workoutTime + breakTime) == 0){
                    tvRounds.setText(count + "/" + rounds);
                    soundPool.play(gong, 1, 1, 0, 0, 1);
                    tvCountdown.setBackgroundResource(R.drawable.break_text_view);
                    isBreak = true;
                    isWorkout = false;
                }
            }

            if (isWorkout){

                if (time[0] < (workoutTime + breakTime)){
                    updateTimer(time[0]);
                }
                else {
                    updateTimer(time[0] % (workoutTime + breakTime)); // that`s how we get 20...19...seconds
                }
            }
            else if (isBreak){

                if (time[0] <= (workoutTime + breakTime)){
                    updateTimer(time[0] % workoutTime);
                }
                else if (time[0] % (workoutTime + breakTime) == 0){
                    updateTimer(breakTime);
                }
                else {
                    int tempTime = time[0] % (workoutTime + breakTime) - workoutTime;
                    updateTimer(tempTime);
                }

                if ((time[0] % (workoutTime + breakTime) - workoutTime) == 3){
                    soundPool.play(countDown, 1, 1, 0, 0, 1);
                }
            }

            if (time[0] == 0){
                soundPool.play(ovation, 1, 1, 0, 0, 1);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            tvCountdown.setBackgroundColor(Color.BLACK);
            tvCountdown.setTextColor(Color.WHITE);
            tvCountdown.setTextSize(100);
            tvCountdown.setText("BRAVO!");
            chronometer.stop();
        }

        @Override
        protected void onCancelled() { super.onCancelled(); }
    }

    private void updateTimer(int currentTime) {

        int minutes = currentTime / 60;
        int seconds = currentTime - (minutes * 60);

        if (seconds < 10){
            tvCountdown.setText(minutes + ":0" + seconds);
        }
        else tvCountdown.setText(minutes + ":" + seconds);
    }

    private void released() {

        soundPool.release();
        soundPool = null;
        soundPoolExercises.release();
        soundPoolExercises = null;
        pauseChronometer = 0;
        countDownTime = 0;
        count = 0;
        isRunning = false;
        isWorkout = false;
        isBreak = false;
        timer = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        released();
    }
}

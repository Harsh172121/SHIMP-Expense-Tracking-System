package com.example.expensetrackersystem;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashScreen extends AppCompatActivity {

    private static final long SPLASH_DELAY_MS = 1500L;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable launchMainRunnable = new Runnable() {
        @Override
        public void run() {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(launchMainRunnable, SPLASH_DELAY_MS);
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(launchMainRunnable);
        super.onPause();
    }
}

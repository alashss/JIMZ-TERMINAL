package com.termux.app;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.termux.R;

/** Branded JIMZ TERMINAL startup screen. */
public final class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DURATION_MS = 1250L;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jimz_splash);

        View content = findViewById(R.id.splash_content);
        TextView status = findViewById(R.id.splash_status);
        TextView dots = findViewById(R.id.splash_dots);

        content.setAlpha(0f);
        content.setScaleX(0.94f);
        content.setScaleY(0.94f);

        AnimatorSet intro = new AnimatorSet();
        intro.playTogether(
            ObjectAnimator.ofFloat(content, View.ALPHA, 0f, 1f),
            ObjectAnimator.ofFloat(content, View.SCALE_X, 0.94f, 1f),
            ObjectAnimator.ofFloat(content, View.SCALE_Y, 0.94f, 1f)
        );
        intro.setDuration(500L);
        intro.setInterpolator(new AccelerateDecelerateInterpolator());
        intro.start();

        final String[] states = {
            "BOOTING JIMZ SHELL",
            "LOADING TERMINAL CORE",
            "READY — STARTING SESSION"
        };
        final int[] step = {0};
        Runnable statusLoop = new Runnable() {
            @Override public void run() {
                status.setText(states[step[0] % states.length]);
                dots.setText(step[0] % 2 == 0 ? "[• • •]" : "[•• •]");
                step[0]++;
                if (step[0] < states.length + 1) handler.postDelayed(this, 280L);
            }
        };
        handler.post(statusLoop);

        handler.postDelayed(() -> {
            startActivity(new Intent(this, TermuxActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, SPLASH_DURATION_MS);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}

package project.mapobed.webhut.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import project.mapobed.webhut.BuildConfig;
import project.mapobed.webhut.R;

public class SplashActivity extends AppCompatActivity {
    private TextView app_version, app_text;
    private ImageView app_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        inflateItems();

        fullScreen();

        app_version.setText(getString(R.string.version) + "     "+BuildConfig.VERSION_NAME + "");

        fade();

        new Handler(Looper.myLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, NormalActivity.class));
                finish();
            }
        }, 2000);
    }

    public void fade() {
        Animation animation =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.fade);
        app_text.startAnimation(animation);
        app_icon.startAnimation(animation);
    }

    private void fullScreen() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void inflateItems() {
        app_version = findViewById(R.id.splash_app_version);
        app_text = findViewById(R.id.splash_app_title);
        app_icon = findViewById(R.id.splash_app_icon);
    }


}
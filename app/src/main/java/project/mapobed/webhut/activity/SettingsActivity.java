package project.mapobed.webhut.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import project.mapobed.webhut.R;

public class SettingsActivity extends AppCompatActivity {
    private ImageView back;
    private RadioGroup radioGroup;
    private RadioButton radioButton, google, bing, yahoo, duck;
    private String engine;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        inflateItems();

        engine = sharedPreferences.getString("search_engine", getString(R.string.google));
        if (engine.equals(getString(R.string.google)))
            google.setChecked(true);
        if (engine.equals(getString(R.string.bing)))
            bing.setChecked(true);
        if (engine.equals(getString(R.string.yahoo)))
            yahoo.setChecked(true);
        if (engine.equals(getString(R.string.duck_duck_go)))
            duck.setChecked(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClick();
            }
        });
    }

    private void inflateItems() {
        sharedPreferences = getSharedPreferences(getString(R.string.shared_preference), MODE_PRIVATE);
        back = findViewById(R.id.settings_back);
        radioGroup = findViewById(R.id.settings_group);
        google = findViewById(R.id.settings_google);
        duck = findViewById(R.id.settings_duck);
        bing = findViewById(R.id.settings_bing);
        yahoo = findViewById(R.id.settings_yahoo);
    }


    public void buttonClick() {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(selectedId);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("search_engine", radioButton.getText().toString());
        editor.apply();
        finish();
    }

    @Override
    public void onBackPressed() {
        buttonClick();
    }
}
package project.mapobed.webhut.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import project.mapobed.webhut.R;

public class HelpActivity extends AppCompatActivity {
private ImageView back;
private RecyclerView help_n_feedback_rec;
private FloatingActionButton feddback_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        inflateItems();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        feddback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HelpActivity.this, getString(R.string.please_wait_feedback_form), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void inflateItems() {

        back=findViewById(R.id.help_back);
        help_n_feedback_rec=findViewById(R.id.help_recycler_view);
        feddback_button=findViewById(R.id.feedback_button);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
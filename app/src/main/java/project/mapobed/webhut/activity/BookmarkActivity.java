package project.mapobed.webhut.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.mapobed.webhut.R;
import project.mapobed.webhut.adapter.BookmarkAdapter;
import project.mapobed.webhut.room.Bookmark;
import project.mapobed.webhut.room.BookmarkViewModel;

public class BookmarkActivity extends AppCompatActivity implements BookmarkAdapter.bookClicked{
    private RecyclerView recyclerView;
    private BookmarkViewModel bookmarkViewModel;
    private BookmarkAdapter adapter;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        inflateItems();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new BookmarkAdapter(this);
        recyclerView.setAdapter(adapter);
        bookmarkViewModel = ViewModelProviders.of(this).get(BookmarkViewModel.class);
        bookmarkViewModel.getAllBookmark().observe(this, new Observer<List<Bookmark>>() {
            @Override
            public void onChanged(List<Bookmark> bookmarkEntities) {
                adapter.setBookmark(bookmarkEntities);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void inflateItems() {
        back=findViewById(R.id.book_back);
        recyclerView = findViewById(R.id.bookmark_rec);
    }

    @Override
    public void book_clicked(String url) {
        Intent in=new Intent();
        in.putExtra("book_url",url);
        setResult(RESULT_OK,in);
        finish();

    }
}
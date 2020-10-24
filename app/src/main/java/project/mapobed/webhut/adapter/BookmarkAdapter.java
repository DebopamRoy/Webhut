package project.mapobed.webhut.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.mapobed.webhut.R;
import project.mapobed.webhut.room.Bookmark;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkHolder> {
    private List<Bookmark> list = new ArrayList<>();
    private bookClicked bookClicked;

    public BookmarkAdapter(BookmarkAdapter.bookClicked bookClicked) {
        this.bookClicked = bookClicked;
    }

    @NonNull
    @Override
    public BookmarkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookmarkHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkHolder holder, int position) {
        Bookmark currentBookmark = list.get(position);
        holder.name.setText(currentBookmark.getBook_name());
        holder.link.setText(currentBookmark.getBook_link());
    }

    public void setBookmark(List<Bookmark> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BookmarkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name, link;

        public BookmarkHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.book_web_item_name);
            link = itemView.findViewById(R.id.book_web_item_link);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            bookClicked.book_clicked(list.get(getAdapterPosition()).getBook_link());
        }
    }
    public interface bookClicked{
         void book_clicked(String url);
    }

}
